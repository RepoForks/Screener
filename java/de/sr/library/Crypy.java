package de.sr.library;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class Crypy {
    public String encrypt(String main) {
        char[] encrypt_array = main.toCharArray();
        String[] encrypt = new String[encrypt_array.length];
        for (int i = 0; i < encrypt_array.length; i++) {
            encrypt[i] = charSet(String.valueOf(encrypt_array[i]));
        }
        StringBuilder builder = new StringBuilder();
        for (String s : encrypt) {
            builder.append(s);
        }
        return builder.toString();
    }

    public String decrypt(String main) {
        String[] decrypt_array = splitInEqualParts(main, main.length() / 4);
        String[] decrypt = new String[decrypt_array.length];
        for (int i = 0; i < decrypt_array.length; i++) {
            decrypt[i] = charSet(decrypt_array[i]);
        }
        StringBuilder builder = new StringBuilder();
        for (String s : decrypt) {
            builder.append(s);
        }
        return builder.toString();
    }

    private static String charSet(String private_char) {
        String all_characters = "???????????????????\u00b6\u00a7?????????? !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~?\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00a2\u00a3\u00a5??\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf?\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb?????????????????????????????????????????????????\u00df????\u00b5??????????\u00b1????\u00f7?\u00b0?\u00b7??\u00b2?";
        try {
            all_characters = new String(all_characters.getBytes("UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String[] characters_arr = splitInEqualParts(all_characters, 254);
        String[] crypt_characters_arr = splitInEqualParts("BfZyGeNYlRseDYsInamkDA7bIX96d5pT1IeJvmRjrnLcp9dNT5zqUuoHYwG53TLyEaREM2zU5hFTFdsUGqCCYoUn7uU2Ssk9mfiPDgGt9YR9CbFg5g8SYBpcUaVlms6yrqELEYaY6fz01M0tOUCzOjwcTc6p5A2gQ7epvWBB9LbenXKmbRITQjQ1nH5gRv8dwtoqTfnOPXr835biru9cRK2u7iKOtOKedmVbSQKMTkgIQY6EUlw5Oec4UbknywdqH56pbZRJtWRXfWseqask2Vi9Tg0zHnh0SY8kMyLVMPF32kcQ3E7QC9VgIYkxq72MgkRM0sn4bGBLQ7XQpwin1jqSA5JlOI3JMv7phnIfniepWLAzjlILK0EwPDVUYfYla5Ukf1opiBJ6LiNmJDcvZdlbEOVWbAyjMjwXYn6hSLa64YEUXkohFWGFQLqWXqVgFqGaPJuscQdgDEevxBzB3wWuJM93KpvWntbqFdtK35EslYRi0II245vubPxpRH4xK8oKcF2JB1TrQlHESmGHSZtISTXRa1vAZFinVr8F7iIrAL1LU3uL95jNe6HiWhzjqPkNOsOLBWYUWFyI922Lw2RgfSjEK17H55kTKqvEklezXno7rttUOECiyORaAV6lAZb00QNny6ii8w7Pf988ofAaS6NFcHmnoIF9akG4PWEXAMXk60cvqeV9UeKWJmzYBItzIdFVgfI6zt8eoUWZW1pULjIMJkh1xZ3szFZJK1wPZaqg3ScwsSdLtbm1REBAiPp1uNYSiHzBdnivjA7dgxt5NR3SD3U4rOALWSooVERPQo1ulCq7QftH1QQJSu7cmur1yR96JOeFrnYFjZK76taUSMyoDsGa1tRaBDksjnvgZd6EBeWEakMlrJowf6YpTCNUfEUrgVK5TjZRYA5LQwslECzYoh8uKGCtYt6QcU6JZobR9sHBLriCzrOXuqM5JxYZ5IEJNsc47MVpoCm0rk9CbAADShKFOB9lUsAloL7blLmOmnPcSuyMeS8cPuAGFMfKyGCqeL3XLjyBZSmzwmYaHt7XXGhLK9xH", 265);
        if (private_char.length() == 1) {
            return crypt_characters_arr[Arrays.asList(characters_arr).indexOf(private_char)];
        }
        return characters_arr[Arrays.asList(crypt_characters_arr).indexOf(private_char)];
    }

    private static String[] splitInEqualParts(String s, int n) {
        if (s == null) {
            return null;
        }
        int strlen = s.length();
        if (strlen < n) {
            throw new IllegalArgumentException("String too short");
        }
        String[] arr = new String[n];
        int tokensize = (strlen / n) + (strlen % n == 0 ? 0 : 1);
        for (int i = 0; i < n; i++) {
            arr[i] = s.substring(i * tokensize, Math.min((i + 1) * tokensize, strlen));
        }
        return arr;
    }
}
