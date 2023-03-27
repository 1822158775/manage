package com.example.manage.util.ai;

import java.awt.Toolkit;
import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
/**
 * @avthor 潘小章
 * @date 2023/3/27
 */


public abstract class StaticMultimediaUtils {
    /**
     * @param encoding
     *                         AudioFormat.Encoding.PCM_SIGNED
     * @param sampleRate
     *                         m_grabber.getSampleRate()
     * @param sampleSizeInBits
     *                         16
     * @param channels
     *                         m_grabber.getAudioChannels()
     * @param frameSize
     *                         m_grabber.getAudioChannels() * 2
     * @param frameRate
     *                         m_grabber.getSampleRate()
     * @param bigEndian
     *                         true
     * @param properties
     * @return
     */
    public static final SourceDataLine createSourceDataLine(Encoding encoding, float sampleRate, int sampleSizeInBits,
                                                            int channels, int frameSize, float frameRate, boolean bigEndian, Map<String, Object> properties) {
        AudioFormat audioFormat;
        audioFormat = new AudioFormat(encoding, sampleRate, sampleSizeInBits, channels, frameSize, frameRate, bigEndian,
                properties);
        return createSourceDataLine(audioFormat);
    }

    /**
     * @param encoding
     *                         AudioFormat.Encoding.PCM_SIGNED
     * @param sampleRate
     *                         m_grabber.getSampleRate()
     * @param sampleSizeInBits
     *                         16
     * @param channels
     *                         m_grabber.getAudioChannels()
     * @param frameSize
     *                         m_grabber.getAudioChannels() * 2
     * @param frameRate
     *                         m_grabber.getSampleRate()
     * @param bigEndian
     *                         true
     * @return
     */
    public static final SourceDataLine createSourceDataLine(Encoding encoding, float sampleRate, int sampleSizeInBits,
                                                            int channels, int frameSize, float frameRate, boolean bigEndian) {
        AudioFormat audioFormat;
        audioFormat = new AudioFormat(encoding, sampleRate, sampleSizeInBits, channels, frameSize, frameRate, bigEndian);
        return createSourceDataLine(audioFormat);
    }

    /**
     * @param sampleRate
     *                         8000
     * @param sampleSizeInBits
     *                         1600
     * @param channels
     *                         2
     * @param signed
     *                         true
     * @param bigEndian
     *                         true
     * @return
     */
    public static final SourceDataLine createSourceDataLine(float sampleRate, int sampleSizeInBits, int channels,
                                                            boolean signed, boolean bigEndian) {
        AudioFormat audioFormat;
        audioFormat = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
        return createSourceDataLine(audioFormat);
    }

    public static final SourceDataLine createSourceDataLine(AudioFormat audioFormat) {
        final DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat, AudioSystem.NOT_SPECIFIED);
        SourceDataLine sourceDataLine = null;
        try {
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            sourceDataLine.open(audioFormat);
            sourceDataLine.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        return sourceDataLine;
    }

    /**
     * @param encoding
     *                         AudioFormat.Encoding.PCM_SIGNED
     * @param sampleRate
     *                         m_grabber.getSampleRate()
     * @param sampleSizeInBits
     *                         16
     * @param channels
     *                         m_grabber.getAudioChannels()
     * @param frameSize
     *                         m_grabber.getAudioChannels() * 2
     * @param frameRate
     *                         m_grabber.getSampleRate()
     * @param bigEndian
     *                         true
     * @param properties
     * @return
     */
    public static final TargetDataLine createTargetDataLine(Encoding encoding, float sampleRate, int sampleSizeInBits,
                                                            int channels, int frameSize, float frameRate, boolean bigEndian, Map<String, Object> properties) {
        AudioFormat audioFormat;
        audioFormat = new AudioFormat(encoding, sampleRate, sampleSizeInBits, channels, frameSize, frameRate, bigEndian,
                properties);
        return createTargetDataLine(audioFormat);
    }

    /**
     * @param encoding
     *                         AudioFormat.Encoding.PCM_SIGNED
     * @param sampleRate
     *                         m_grabber.getSampleRate()
     * @param sampleSizeInBits
     *                         16
     * @param channels
     *                         m_grabber.getAudioChannels()
     * @param frameSize
     *                         m_grabber.getAudioChannels() * 2
     * @param frameRate
     *                         m_grabber.getSampleRate()
     * @param bigEndian
     *                         true
     * @return
     */
    public static final TargetDataLine createTargetDataLine(Encoding encoding, float sampleRate, int sampleSizeInBits,
                                                            int channels, int frameSize, float frameRate, boolean bigEndian) {
        AudioFormat audioFormat;
        audioFormat = new AudioFormat(encoding, sampleRate, sampleSizeInBits, channels, frameSize, frameRate, bigEndian);
        return createTargetDataLine(audioFormat);
    }

    /**
     * @param sampleRate
     *                         8000
     * @param sampleSizeInBits
     *                         1600
     * @param channels
     *                         2
     * @param signed
     *                         true
     * @param bigEndian
     *                         true
     * @return
     */
    public static final TargetDataLine createTargetDataLine(float sampleRate, int sampleSizeInBits, int channels,
                                                            boolean signed, boolean bigEndian) {
        AudioFormat audioFormat;
        audioFormat = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
        return createTargetDataLine(audioFormat);
    }

    public static final TargetDataLine createTargetDataLine(AudioFormat audioFormat) {
        final DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat, AudioSystem.NOT_SPECIFIED);
        TargetDataLine targetDataLine = null;
        try {
            targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
            targetDataLine.open(audioFormat);
            targetDataLine.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        return targetDataLine;
    }

    public static final void printSound(SourceDataLine sourceDataLine, byte[] sound, int from, int length) {
        sourceDataLine.write(sound, from, length);
    }

    public static final void printSound(SourceDataLine sourceDataLine, byte[] sound) {
        sourceDataLine.write(sound, 0, sound.length);
    }

    public static final int scanSound(TargetDataLine targetDataLine, byte[] sound, int from, int length) {
        return targetDataLine.read(sound, from, length);
    }

    public static final int scanSound(TargetDataLine targetDataLine, byte[] sound) {
        return targetDataLine.read(sound, 0, sound.length);
    }

    public static final byte[] scanSound(TargetDataLine targetDataLine) {
        int available = targetDataLine.available();
        byte[] sound = new byte[available];
        targetDataLine.read(sound, 0, available);
        return sound;
    }

    public static final byte[] mixedDoubleChannel(byte[] left, byte[] right) {
        byte[] combine = new byte[left.length + right.length];
        for (int i = 0, c = 0; i < left.length;) {
            combine[c++] = left[i++];
            combine[c++] = left[i--];// 回到这个位置，再读取right的这个位置
            combine[c++] = right[i++];
            combine[c++] = right[i++];
        }
//		int k = 0;
//		for (int i = 0; i < left.length; i = i + 2) {// 混合两个声道。
//			for (int j = 0; j < 2; j++) {
//				combine[j + 4 * k] = left[i + j];
//				combine[j + 2 + 4 * k] = right[i + j];
//			}
//			k++;
//		}
        return combine;
    }

    /**
     * @param combine
     *                0是left，1是right
     * @return
     */
    public static final byte[][] seperateDoubleChannel(byte[] combine) {
        byte[][] ret = new byte[][] { new byte[combine.length / 2], new byte[combine.length / 2] };
        for (int i = 0, c = 0; c < combine.length;) {
            ret[0][i++] = combine[c++];
            ret[0][i--] = combine[c++];
            ret[1][i++] = combine[c++];
            ret[1][i++] = combine[c++];
        }
        return ret;
    }

    /**
     * 系统提示音
     */
    public static final void printSystemBeep() {
        Toolkit.getDefaultToolkit().beep();
    }
}
