package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TranslateClass {
    private String name;

    public TranslateClass(String name) {
        this.name = name;
    }

    public static List<List<Byte>> splitFunc(List<Byte> lst, int sz) {
        List<List<Byte>> result = new ArrayList<>();
        for (int i = 0; i < lst.size(); i += sz) {
            result.add(lst.subList(i, Math.min(lst.size(), i + sz)));
        }
        return result;
    }

    public void translate() throws IOException {
        try (InputStream file = new FileInputStream(name)) {
            byte[] data = new byte[4096];
            int bytesRead = file.read(data);
            List<Byte> dicValue = new ArrayList<>();
            for (int i = 0; i < bytesRead; i++) {
                dicValue.add(data[i]);
            }
            List<List<Byte>> count = splitFunc(dicValue, 16);
            List<String> outData = new ArrayList<>();
            for (int numb = 0; numb < 256; numb++) {
                System.out.println(count.get(numb) + " ------> " + numb);
                outData.add(count.get(numb) + ", " + numb);
            }
            try (Writer fw = new BufferedWriter(new FileWriter("dic_file.txt"))) {
                for (String line : outData) {
                    fw.write(line + "\n");
                }
            }
        }
    }
}

