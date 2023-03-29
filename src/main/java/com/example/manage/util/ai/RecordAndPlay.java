package com.example.manage.util.ai;

import com.example.manage.util.CodeGeneration;
import com.example.manage.util.LangDu;
import lombok.extern.slf4j.Slf4j;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * TargetDataLine是声音的输入(麦克风),而SourceDataLine是输出(音响,耳机).
 * @avthor 潘小章
 * @date 2023/3/27
 */
@Slf4j
public class RecordAndPlay {

    static volatile boolean stop = false;

    public static void main(String[] args) {
        Wavdemo.WaveformGraph waveformGraph = new Wavdemo.WaveformGraph(500, 300);
        waveformGraph.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        waveformGraph.setVisible(true);

        try {
            AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100F, 16, 2, 4, 44100F, true);

            DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);

            TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
            targetDataLine.open(audioFormat);

            final SourceDataLine sourceDataLine;
            info = new DataLine.Info(SourceDataLine.class, audioFormat);

            sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceDataLine.open(audioFormat);

            targetDataLine.start();
            sourceDataLine.start();
            FloatControl fc = (FloatControl) sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
            double value = 2;
            float dB = (float) (Math.log(value == 0.0 ? 0.0001 : value) / Math.log(10.0) * 20.0);
            fc.setValue(dB);
            int nByte = 0;
            final int bufSize = 4*100;
            byte[] buffer = new byte[bufSize];
            while (nByte != -1) {
                nByte = targetDataLine.read(buffer, 0, bufSize);
                if (buffer[0] > 3){
                    log.info("buffer:{}",buffer.length);
                }
                sourceDataLine.write(buffer, 0, nByte);
                waveformGraph.put((short) ((buffer[1]) | buffer[0]));
            }
            //sourceDataLine.stop();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class WaveformGraph extends JFrame {

        private LinkedList<Short> deque = new LinkedList<Short>();
        private java.util.Timer timer;
        private Image buffered;
        private Image showing;

        public WaveformGraph(int width, int height) {
            setSize(width, height);
            timer = new Timer();
            buffered = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {

                    Graphics g = buffered.getGraphics();
                    g.setColor(Color.WHITE);
                    g.fillRect(0, 0, getWidth(), getHeight());
                    g.setColor(Color.BLACK);

                    g.translate(10, getHeight() / 2);

                    synchronized (deque) {
                        float heightRate = 1;
                        if (deque.size() > 1) {
                            Iterator<Short> iter = deque.iterator();
                            Short p1 = iter.next();
                            Short p2 = iter.next();
                            int x1 = 0, x2 = 0;
                            while (iter.hasNext()) {
                                g.drawLine(x1, (int) (p1 * heightRate), x2, (int) (p2 * heightRate));

                                p1 = p2;
                                p2 = iter.next();
                                x1 = x2;
                                x2 += 1;
                            }
                        }
                    }
                    g.dispose();

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            showing = buffered;
                            repaint();
                            showing = null;
                        }
                    });
                }
            }, 100, 100);
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            if (buffered != null) {
                g.drawImage(buffered, 0, 0, null);
            }
        }

        public void put(short v) {
            synchronized (deque) {
                deque.add(v);
                if (deque.size() > 500) {
                    deque.removeFirst();
                }
            }
        }

        public void clear() {
            deque.clear();
        }
    }
}
