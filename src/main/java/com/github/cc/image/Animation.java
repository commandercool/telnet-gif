package com.github.cc.image;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.github.cc.decoder.GifDecoder;

public class Animation {

    private static final char[] PIXEL = {' ', '.', '*', ':', 'o', '&', '8', '#', '@'};
    private static final int STEP = 255 / 9;

    private static Frame[] frames;

    public static void init() {
        try (InputStream resourceAsStream = new BufferedInputStream(
                ClassLoader.getSystemClassLoader().getResourceAsStream("card-cropped.gif"))) {
            final GifDecoder.GifImage gif = GifDecoder.read(resourceAsStream);
            final int frameCount = gif.getFrameCount();
            frames = new Frame[frameCount];
            for (int i = 0; i < frameCount; i++) {
                final BufferedImage img = gif.getFrame(i);
                frames[i] = new Frame(convertToASCII(prepare(img)), gif.getDelay(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static BufferedImage prepare(BufferedImage orig) {
        double scale = 0.1;
        BufferedImage bi = new BufferedImage((int) (orig.getWidth() * scale), (int) (orig.getHeight() * scale),
                BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D grph = (Graphics2D) bi.getGraphics();
        grph.scale(scale, scale);
        grph.drawImage(orig, 0, 0, null);
        return bi;
    }

    private static String convertToASCII(BufferedImage image) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                int intensity = (rgb & 0xff);
                char c = PIXEL[intensity / STEP];
                if (c == '#') {
                    c = ' ';
                }
                stringBuilder.append(c);
            }
            stringBuilder.append("\n\r");
        }
        return stringBuilder.toString();
    }

    public static Frame[] getFrames() {
        return frames;
    }

    public static class Frame {

        private final String asciiImage;

        private final int delayTime;

        Frame(String asciiImage, int delayTime) {
            this.asciiImage = asciiImage;
            this.delayTime = delayTime;
        }

        public String getAsciiImage() {
            return this.asciiImage;
        }

        public int getDelayTime() {
            return this.delayTime;
        }

    }

}
