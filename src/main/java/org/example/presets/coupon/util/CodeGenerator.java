package org.example.presets.coupon.util;

import java.util.Random;

public class CodeGenerator {
    private static final String asciiPattern = "ZXCVASDFQWER";

    public static String generateCodeById(Long id) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            long asciiCode = (id / (long) Math.pow(10, i)) % 10 + asciiPattern.charAt(i);
            if (asciiCode > 90) {
                asciiCode = asciiCode - 26;
            }
            stringBuilder.append((char) asciiCode);
        }

        Random random = new Random();
        stringBuilder.insert(2, (char) (random.nextInt(26) + 'A'));
        stringBuilder.insert(6, (char) (random.nextInt(26) + 'A'));
        stringBuilder.insert(10, (char) (random.nextInt(26) + 'A'));
        stringBuilder.insert(14, (char) (random.nextInt(26) + 'A'));

        return stringBuilder.toString();
    }
}
