package org.example;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class EncodeClass {
    private String name;
    private byte[] key;

    public EncodeClass(String name, String key) throws Exception {
        this.name = name;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        this.key = digest.digest(key.getBytes("UTF-8"));
    }

    private static List<byte[]> splitFunc(byte[] lst, int sz) {
        List<byte[]> result = new ArrayList<>();
        for (int i = 0; i < lst.length; i += sz) {
            int length = Math.min(sz, lst.length - i);
            byte[] part = new byte[length];
            System.arraycopy(lst, i, part, 0, length);
            result.add(part);
        }
        return result;
    }

    public void encode() throws Exception {
        try (FileInputStream fileInp = new FileInputStream(this.name)) {
            byte[] data = fileInp.readAllBytes();
            List<byte[]> outData = new ArrayList<>();
            List<byte[]> count = splitFunc(data, 16);
            SecretKeySpec secretKey = new SecretKeySpec(this.key, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            for (byte[] i : count) {
                outData.add(cipher.doFinal(i));
            }
            try (FileOutputStream fileOut = new FileOutputStream(this.name)) {
                for (byte[] oneData : outData) {
                    fileOut.write(oneData);
                }
            }
        }
    }
}

