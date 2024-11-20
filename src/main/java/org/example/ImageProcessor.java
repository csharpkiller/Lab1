package org.example;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ImageProcessor {
    private static List<byte[]> masByte = new ArrayList<>();
    private static List<byte[]> dicByte = new ArrayList<>();
    private static List<byte[]> exitByte = new ArrayList<>();
    private static List<byte[]> outByte = new ArrayList<>();
    private static List<Byte> dictFirstByte = new ArrayList<>();
    private static List<Object[]> dictTransByte = new ArrayList<>();

    public static void extensionPicture(String name) throws IOException {
        byte[] data = Files.readAllBytes(Paths.get(name));
        for (byte b : data) {
            byte[] arr = new byte[16];
            arr[0] = b;
            masByte.add(arr);
        }
        for (int clearByte = 0; clearByte < 256; clearByte++) {
            byte[] arr = new byte[16];
            arr[0] = (byte) clearByte;
            dicByte.add(arr);
        }

        try (FileOutputStream file = new FileOutputStream(name)) {
            for (byte[] byteArr : dicByte) {
                file.write(byteArr);
            }
            for (byte[] byteArr : masByte) {
                file.write(byteArr);
            }
        }
    }

    public static String randKey(int length) {
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append((char) ('a' + random.nextInt(26)));
        }
        return sb.toString();
    }

    public static String convertBase(int num, int toBase, int fromBase) {
        String symbols = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        if (num < toBase) {
            return String.valueOf(symbols.charAt(num));
        } else {
            return convertBase(num / toBase, toBase, fromBase) + symbols.charAt(num % toBase);
        }
    }

    public static void fileEncryption(String name) throws Exception {
        String keyString = randKey(new Random().nextInt(7) + 8);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] key = digest.digest(keyString.getBytes());

        byte[] data = Files.readAllBytes(Paths.get(name));
        for (int i = 0; i < data.length; i += 16) {
            byte[] block = new byte[16];
            System.arraycopy(data, i, block, 0, Math.min(16, data.length - i));
            exitByte.add(block);
        }

        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        for (byte[] g : exitByte) {
            outByte.add(cipher.doFinal(g));
        }

        try (FileOutputStream file = new FileOutputStream(name)) {
            for (byte[] byteArr : outByte) {
                file.write(byteArr);
            }
        }
    }

    public static void dictCreate(String name) throws IOException {
        byte[] data = Files.readAllBytes(Paths.get(name));
        byte[] dictinory = new byte[4096];
        System.arraycopy(data, 0, dictinory, 0, Math.min(data.length, 4096));
        for (byte b : dictinory) {
            dictFirstByte.add(b);
        }

        List<byte[]> exitBytes = new ArrayList<>();
        for (int i = 0; i < dictFirstByte.size(); i += 16) {
            byte[] block = new byte[16];
            for (int j = 0; j < 16 && (i + j) < dictFirstByte.size(); j++) {
                block[j] = dictFirstByte.get(i + j);
            }
            exitBytes.add(block);
        }

        for (int g = 0; g < 256; g++) {
            System.out.println(g + " : " + exitBytes.get(g));
            dictTransByte.add(new Object[]{exitBytes.get(g), g});
        }

        try (Writer writer = new FileWriter("dict.txt")) {
            Gson gson = new Gson();
            gson.toJson(dictTransByte, writer);
        }
    }

    public static void decodingPic(String name) throws IOException {
        List<Integer> outMas = new ArrayList<>();
        try (Reader reader = new FileReader("dict.txt")) {
            Gson gson = new Gson();
            dictTransByte = gson.fromJson(reader, new TypeToken<List<Object[]>>(){}.getType());
        }

        byte[] data = Files.readAllBytes(Paths.get(name));
        byte[] dataByte = new byte[data.length - 4096];
        System.arraycopy(data, 4096, dataByte, 0, dataByte.length);
        List<Byte> valueDiction = new ArrayList<>();
        for (byte b : dataByte) {
            valueDiction.add(b);
        }

        List<byte[]> valueDictionory = new ArrayList<>();
        for (int i = 0; i < valueDiction.size(); i += 16) {
            byte[] block = new byte[16];
            for (int j = 0; j < 16 && (i + j) < valueDiction.size(); j++) {
                block[j] = valueDiction.get(i + j);
            }
            valueDictionory.add(block);
        }

        for (byte[] valueFirst : valueDictionory) {
            for (Object[] valueSecond : dictTransByte) {
                /*if (Arrays.equals(valueFirst, (byte[]) valueSecond[0])) {
                    outMas.add((Integer) valueSecond[1]);
                }*/
                byte[] arr = convToByte(valueSecond[0]);
                var s = valueSecond[0];
                if (Arrays.equals(valueFirst, arr)) {
                    //outMas.add((Integer) valueSecond[1]);
                    outMas.add(Integer.parseInt(valueSecond[1].toString().substring(0, valueSecond[1].toString().indexOf("."))));
                }
            }
        }
        System.out.println("123");
        try (FileOutputStream file = new FileOutputStream(name)) {
            for (int j : outMas) {
                String result = convertBase(j, 16, 10);
                if (!result.equals("0")) {
                    if (result.length() % 2 == 0) {
                        file.write(hexStringToByteArray(result));
                    } else {
                        result = '0' + result;
                        file.write(hexStringToByteArray(result));
                    }
                } else {
                    file.write(0);
                }
            }
        }
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static byte[] convertObjectToBytes2(Object obj) throws IOException {
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        try (ObjectOutputStream ois = new ObjectOutputStream(boas)) {
            ois.writeObject(obj);
            return boas.toByteArray();
        }
    }

    private static byte[] convToByte(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Object> lst = objectMapper.readValue(obj.toString(), List.class);
        ArrayList<Object> byteArrayList = new ArrayList<>(lst);

        byte[] res = new byte[lst.size()];
        for(int i = 0; i < lst.size(); i++){
            String str = byteArrayList.get(i).toString();
            str = str.substring(0, str.indexOf("."));
            res[i] = Byte.parseByte(str);
            //res[i] = (byte) byteArrayList.get(i);
        }
        return res;
    }

    public static void main(String[] args) throws Exception {
        extensionPicture("test.jpg");
        System.out.println("1 выполнен");
        fileEncryption("test.jpg");
        System.out.println("2 выполнен");
        dictCreate("test.jpg");
        System.out.println("3 выполнен");
        decodingPic("test.jpg");
        System.out.println("4 выполнен");
    }


}

