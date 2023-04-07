package com.example.manage.util.ai;

import javax.sound.sampled.*;
import java.io.*;
import java.util.Arrays;

/**
 * @avthor 潘小章
 * @date 2023/4/4
 */

public class MicrophoneRecorder {
    private static final int SAMPLE_RATE = 16000;
    private static final int SAMPLE_SIZE_IN_BITS = 16;
    private static final int CHANNELS = 1;
    private static final boolean SIGNED = true;
    private static final boolean BIG_ENDIAN = true;
    private static final AudioFormat OUTPUT_FORMAT = new AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED, 16000, 16, 1, 2, 16000, false);
    public static void main(String[] args) throws Exception {
        AudioFormat format = new AudioFormat(SAMPLE_RATE, SAMPLE_SIZE_IN_BITS, CHANNELS, SIGNED, BIG_ENDIAN);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
        line.open(format);
        line.start();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[line.getBufferSize() / 5];

        while (true) {
            int count = line.read(buffer, 0, buffer.length);
            if (count > 0) {
                out.write(buffer, 0, count);
                byte[] data = out.toByteArray();
                if (data.length > 16000) {
                    // 将音频数据转换为语音识别API支持的音频格式
                    byte[] audio = convertAudio(data);
                    // 使用语音识别API将音频转换为文字
                    String text = recognizeAudio(audio);
                    // 处理转换后的文字
                    System.out.println(text);
                    // 清空缓冲区
                    out.reset();
                }
            }
        }
    }

    private static byte[] convertAudio(byte[] data) throws IOException {
        // TODO: 将音频数据转换为语音识别API支持的音频格式
        AudioFormat inputFormat = new AudioFormat(8000, 16, 1, true, false);
        AudioInputStream input = new AudioInputStream(
                new ByteArrayInputStream(data), inputFormat, data.length / 2);

        AudioInputStream output = AudioSystem.getAudioInputStream(OUTPUT_FORMAT, input);
        byte[] result = new byte[output.available()];
        output.read(result);
        return result;
    }
    /**
     * 拼接byte[]
     *
     * @param orig 原始byte[]
     * @param dest 需要拼接的数据
     * @return byte[]
     */
    public static byte[] append(byte[] orig, byte[] dest) {

        byte[] newByte = new byte[orig.length + dest.length];

        System.arraycopy(orig, 0, newByte, 0, orig.length);
        System.arraycopy(dest, 0, newByte, orig.length, dest.length);

        return newByte;

    }
    private static String recognizeAudio(byte[] audio) {
        // TODO: 使用语音识别API将音频转换为文字
        System.out.println(audio);
        return "";
    }
}
