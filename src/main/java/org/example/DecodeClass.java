package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DecodeClass {
    private String name;

    public DecodeClass(String name) {
        this.name = name;
    }

    public static List<List<Integer>> splitFunc(List<Integer> lst, int sz) {
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < lst.size(); i += sz) {
            result.add(lst.subList(i, Math.min(i + sz, lst.size())));
        }
        return result;
    }

    public static String convertBase(Object num, int toBase, int fromBase) {
        int n;
        if (num instanceof String) {
            n = Integer.parseInt((String) num, fromBase);
        } else {
            n = (int) num;
        }
        String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        if (n < toBase) {
            return String.valueOf(alphabet.charAt(n));
        } else {
            return convertBase(n / toBase, toBase, fromBase) + alphabet.charAt(n % toBase);
        }
    }

    public void decode() throws IOException {
        List<String> outText = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        List<List<Object>> lst = objectMapper.readValue(new File("dic_file.txt"), List.class);

        try (FileInputStream fileInputStream = new FileInputStream(this.name)) {
            byte[] data = new byte[(int) new File(this.name).length()];
            fileInputStream.read(data);
            List<Integer> dicValue = new ArrayList<>();
            for (int i = 4096; i < data.length; i++) {
                dicValue.add((int) data[i]);
            }
            List<List<Integer>> dataText = splitFunc(dicValue, 16);
            for (List<Integer> values : dataText) {
                for (List<Object> value : lst) {
                    if (values.equals(value.get(0))) {
                        outText.add((String) value.get(1));
                    }
                }
            }
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(this.name)) {
            for (String oneData : outText) {
                String res = convertBase(oneData, 16, 10);
                if (!res.equals("0")) {
                    if (res.length() % 2 == 0) {
                        fileOutputStream.write(hexStringToByteArray(res));
                    } else {
                        res = '0' + res;
                        fileOutputStream.write(hexStringToByteArray(res));
                    }
                } else {
                    fileOutputStream.write(new byte[0]);
                }
            }
        }
    }

    private byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}

