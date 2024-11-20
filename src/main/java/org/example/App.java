package org.example;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/**
 * Hello world!
 */
public class App {
    private static SecretKeySpec secretKeySpec;
    /*public static String generateRandomString(int length) {
        String letters = "abcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        StringBuilder randString = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            randString.append(letters.charAt(random.nextInt(letters.length())));
        }
        return randString.toString();
    }

    public static void main(String[] args) throws Exception {
        PrepareClass prepare = new PrepareClass("test.jpg");
        prepare.prepare();
        EncodeClass encode = new EncodeClass("test.jpg", generateRandomString(8));
        encode.encode();
        TranslateClass translate = new TranslateClass("test.jpg");
        translate.translate();
        DecodeClass decode = new DecodeClass("test.jpg");
        decode.decode();
    }*/

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        /*byte[] res = extractBytes("test.jpg");
        //byte[] res1 = extractBytes("test1.jpg");

        BufferedImage img = ImageIO.read(new ByteArrayInputStream(res));
        ImageIO.write(img, "jpg", new File("test1"));

        *//*try (OutputStream out = new BufferedOutputStream(new FileOutputStream("test1.jpg"))) {
            out.write(res);
        }*//*

        System.out.println(res);*/

       /* BufferedImage image = ImageIO.read(new File("test.jpg"));

        // create the object of ByteArrayOutputStream class
        ByteArrayOutputStream outStreamObj = new ByteArrayOutputStream();

        // write the image into the object of ByteArrayOutputStream class
        ImageIO.write(image, "jpg", outStreamObj);

        // create the byte array from image
        byte [] byteArray = outStreamObj.toByteArray();

        // create the object of ByteArrayInputStream class
        // and initialized it with the byte array.
        ByteArrayInputStream inStreambj = new ByteArrayInputStream(byteArray);

        // read image from byte array
        BufferedImage newImage = ImageIO.read(inStreambj);

        // write output image
        ImageIO.write(newImage, "jpg", new File("outputImage.jpg"));
        System.out.println("Image generated from the byte array.");*/
        /*String keyString = randKey(new Random().nextInt(7) + 8);
        byte[] key = MessageDigest.getInstance("SHA-256").digest(keyString.getBytes("UTF-8"));
        secretKeySpec = new SecretKeySpec(key, "AES");


        byte[] imageBytes = imagaToByteArr("test.jpg");
        byte[] expandBytes = expandByteArr(imageBytes, 16);
        byteToFile(expandBytes, "dic.jpg");
        byte[] read = fileToByteArr("dic.jpg");
        byte[] encodeBytes = encodeBytes(read);
        //byte[] decodedBytes = decodeBytes(encodeBytes);
        fuckoff(read);
*/
        String name = "test.jpg";
        prepare(name);
        encode(name);
        decode(name);
    }

    public static void prepare(String name){
        byte[] imageBytes = imagaToByteArr(name);
        byteToFile(expandByteArr(imageBytes, 16), "prepare"+name);

        byte[] dic = new byte[256];
        for(int i = 0; i < dic.length; i++){
            dic[i] = (byte)i;
        }
        byteToFile(expandByteArr(dic, 16), "prepareDic"+name);
    }

    public static void encode(String name) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String keyString = randKey(new Random().nextInt(7) + 8);
        byte[] key = MessageDigest.getInstance("SHA-256").digest(keyString.getBytes("UTF-8"));
        secretKeySpec = new SecretKeySpec(key, "AES");

        byte[] imageBytes = fileToByteArr("prepare"+name);
        byte[] dicBytes = fileToByteArr("prepareDic"+name);

        byteToFile(encodeBytes(imageBytes), "encode"+name);
        byteToFile(encodeBytes(dicBytes), "encdoeDic"+name);
    }

    public static void decode(String name){
        byte[] toDecode = fileToByteArr("encode"+name);
        byte[] decoded = decodeBytes(toDecode, "encdoeDic"+name);

        byteToImageFile(decoded, "decoded");
    }


    private static byte[] imagaToByteArr(String fileName){
        BufferedImage image = null;
        ByteArrayOutputStream outStreamObj = new ByteArrayOutputStream();
        try {
            image = ImageIO.read(new File(fileName));
            //ByteArrayOutputStream outStreamObj = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", outStreamObj);
        } catch (IOException e) {
            e.fillInStackTrace();
            return new byte[1];
        }
        //ByteArrayOutputStream outStreamObj = new ByteArrayOutputStream();
        byte [] byteArray = outStreamObj.toByteArray();
        return byteArray;
    }

    private static void byteToImageFile(byte[] bytes, String fileName){
        ByteArrayInputStream inStreambj = new ByteArrayInputStream(bytes);
        try {
            BufferedImage newImage = ImageIO.read(inStreambj);
            ImageIO.write(newImage, "jpg", new File(fileName));
            System.out.println("Image generated from the byte array.");
        } catch (IOException e) {
            e.fillInStackTrace();
        }
    }

    private static void byteToFile(byte[] bytes, String fileName){
        try(FileOutputStream fos = new FileOutputStream(fileName)){
            fos.write(bytes);
        } catch (IOException e) {
            e.fillInStackTrace();
        }
    }

    private static byte[] fileToByteArr(String fileName){
        byte[] result = new byte[1];
        try {
            result = Files.readAllBytes(Paths.get(fileName));
        } catch (IOException e) {
            e.fillInStackTrace();
        }
        return result;
    }

    private static byte[] expandByteArr(byte[] bytes, int n){
        byte[] expendedArr = new byte[bytes.length+ bytes.length*(n-1)];
        Arrays.fill(expendedArr, (byte) 0);
        int counter = 0;
        int leng = bytes.length;
        for(int i = 0; i < expendedArr.length; i+=(n)){
            expendedArr[i] = bytes[counter];
            counter++;
        }
        return expendedArr;
    }

    private static byte[] encodeBytes(byte[] bytes){
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

    private static String randKey(int length) {
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append((char) ('a' + random.nextInt(26)));
        }
        return sb.toString();
    }

    private static byte[] decodeBytes(byte[] bytes, String dicName){
        byte[] dic = fileToByteArr(dicName);
        byte[] result = new byte[dic.length];
        /*HashMap<byte[], Byte> hashMap = new HashMap<>();
        byte[] result = new byte[dic.length];
        byte counter = 0;
        for(int i = 0; i < dic.length; i+=16){
            byte[] value = new byte[15];
            for(int j = 0; j < 15; j++){
                value[j] = dic[j+i];
            }
            hashMap.put(value, counter);
            counter++;
        }*/
        HashMap<String, Byte> hashMap = fuckoff(dic);

        int counter=1;
        for(int i = 1; i < bytes.length; i+=16){
            counter+=16;
            if(counter>=4111) break;
            byte[] value = new byte[15];
            for(int j = 0; j < 15; j++){
                value[j] = bytes[j+i];
            }
            String valueStr = Arrays.toString(value);
            result[i-1] = hashMap.get(valueStr);
        }
        return result;
    }

    private static HashMap<String, Byte> fuckoff(byte[] bytes){
        HashMap<String, Byte> hashMap = new HashMap<>();
        byte[] result = new byte[bytes.length];
        byte counter = 0;
        for(int i = 1; i < bytes.length; i+=16){
            counter--;
            System.out.print(counter + "---> [");
            byte[] value = new byte[15];
            for(int j = 0; j < 15; j++){
                value[j] = bytes[j+i];
                System.out.print(value[j]+" ");
            }
            hashMap.put(Arrays.toString(value), counter);

            counter++;
            System.out.print("]");
            System.out.println();
        }
        return hashMap;
    }

    private byte[] unPrepare(byte[] bytes){
        byte[] result = new byte[bytes.length/16];
        int counter = 0;
        for(int i = 0; i < bytes.length; i+=15){
            result[counter] = bytes[i];
            counter++;
        }
        return result;
    }
}
