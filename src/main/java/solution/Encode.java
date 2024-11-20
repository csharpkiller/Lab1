package solution;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Encode {
    private SecretKeySpec secretKeySpec;

    public Encode(){
        String keyString = randKey(new Random().nextInt(7) + 8);
        byte[] key = null;
        try {
            key = MessageDigest.getInstance("SHA-256").digest(keyString.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        secretKeySpec = new SecretKeySpec(key, "AES");
    }


    /**
     * Зашифровать файл (AES)
     */
    public void encode(String fileToEncdoe, String outputFile){
        IOBytes ioBytes = new IOBytes();
        ioBytes.writeBytes(encodeBytes(ioBytes.readFile(fileToEncdoe)), outputFile);
    }

    private byte[] encodeBytes(byte[] bytes){
        try {
            byte[] encodeBytes = bytes.clone();
            /*String keyString = randKey(new Random().nextInt(7) + 8);
            byte[] key = MessageDigest.getInstance("SHA-256").digest(keyString.getBytes("UTF-8"));
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");*/
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            encodeBytes = cipher.doFinal(bytes);
            return encodeBytes;
        }
        catch (Exception e){
            e.fillInStackTrace();
        }
        return new byte[1];
    }

    private String randKey(int length) {
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append((char) ('a' + random.nextInt(26)));
        }
        return sb.toString();
    }
}
