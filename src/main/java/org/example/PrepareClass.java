package org.example;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

class PrepareClass {
    private String name;

    public PrepareClass(String name) {
        this.name = name;
    }

    public void prepare() throws IOException {
        byte[][] dicSort = new byte[1000][16];
        byte[][] writeData = new byte[1000][16];

        try (FileInputStream file = new FileInputStream(this.name)) {
            int data;
            int index = 0;
            while ((data = file.read()) != -1) {
                writeData[index] = new byte[]{(byte) data, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                index++;
            }
        }


        for (int k = 0; k < 256; k++) {
            dicSort[k] = new byte[]{(byte) k, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        }

        try (FileOutputStream file = new FileOutputStream(this.name)) {
            for (byte[] key : dicSort) {
                file.write(key);
            }
            for (byte[] oneData : writeData) {
                file.write(oneData);
            }
        }
    }
}

