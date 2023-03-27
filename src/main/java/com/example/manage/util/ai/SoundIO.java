package com.example.manage.util.ai;

import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import java.io.Closeable;

/**
 * @avthor 潘小章
 * @date 2023/3/27
 */

public class SoundIO implements Closeable {
    private SourceDataLine m_output = null;
    private TargetDataLine m_input = null;

    public SoundIO() {

    }

    public SoundIO(float sampleRate, int sampleSizeInBits, int channels, boolean signed, boolean bigEndian) {
        open(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }

    public void open(float sampleRate, int sampleSizeInBits, int channels, boolean signed, boolean bigEndian) {
        openInput(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
        openOutput(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }

    public void openInput(float sampleRate, int sampleSizeInBits, int channels, boolean signed, boolean bigEndian) {
        if (m_input != null) {
            return;
        }
        m_input = StaticMultimediaUtils.createTargetDataLine(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }

    public void openOutput(float sampleRate, int sampleSizeInBits, int channels, boolean signed, boolean bigEndian) {
        if (m_output != null) {
            return;
        }
        m_output = StaticMultimediaUtils.createSourceDataLine(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }

    public byte[] read() {
        if (m_input == null) {
            System.err.println("音频输入设备没有打开");
            return null;
        }
        return StaticMultimediaUtils.scanSound(m_input);
    }

    public int read(byte[] sound) {
        if (m_input == null) {
            System.err.println("音频输入设备没有打开");
            return -1;
        }
        return StaticMultimediaUtils.scanSound(m_input, sound);
    }

    public int read(byte[] sound, int from, int length) {
        if (m_input == null) {
            System.err.println("音频输入设备没有打开");
            return -1;
        }
        return StaticMultimediaUtils.scanSound(m_input, sound, from, length);
    }

    public boolean write(byte[] sound) {
        if (m_output == null) {
            System.err.println("音频输出设备没有打开");
            return false;
        }
        StaticMultimediaUtils.printSound(m_output, sound);
        return true;
    }

    public boolean write(byte[] sound, int from, int length) {
        if (m_output == null) {
            System.err.println("音频输出设备没有打开");
            return false;
        }
        StaticMultimediaUtils.printSound(m_output, sound, from, length);
        return true;
    }

    public void closeInput() {
        m_input = null;
    }

    public void closeOutput() {
        m_output = null;
    }

    @Override
    public void close() {
        closeInput();
        closeOutput();
    }
}