package solution;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class IOBytes {

    /**
     * Конвертирует байты в файл
     * @param bytes
     * @param fileName
     */
    public void writeBytes(byte[] bytes, String fileName, String formatName){
        switch (formatName){
            case "jpg":
            {
                ByteArrayInputStream inStreambj = new ByteArrayInputStream(bytes);
                try {
                    BufferedImage newImage = ImageIO.read(inStreambj);
                    ImageIO.write(newImage, formatName, new File(fileName+".jpg"));
                    System.out.println("Image generated from the byte array.");

                } catch (IOException e) {
                    e.fillInStackTrace();
                }
            }
            default:{
                try (FileOutputStream fos = new FileOutputStream(fileName)) {
                    fos.write(bytes);
                } catch (IOException e) {
                    System.out.println("wtf");
                }

            }
        }
    }

    public void writeBytes(byte[] bytes, String fileName){
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(bytes);
        } catch (IOException e) {
            System.out.println("ЭэЭЭыфэыфаэф");
        }
    }

    /**
     * Читает и конвертирует файл в массив байтов
     * @param fileName
     * @return
     */
    public byte[] readFile(String fileName, String formatName){
        switch (formatName){
            case "jpg":{
                BufferedImage image = null;
                ByteArrayOutputStream outStreamObj = new ByteArrayOutputStream();
                try {
                    image = ImageIO.read(new File(fileName+".jpg"));
                    //ByteArrayOutputStream outStreamObj = new ByteArrayOutputStream();
                    ImageIO.write(image, formatName, outStreamObj);
                } catch (IOException e) {
                    e.fillInStackTrace();
                    return new byte[1];
                }
                //ByteArrayOutputStream outStreamObj = new ByteArrayOutputStream();
                byte [] byteArray = outStreamObj.toByteArray();
                return byteArray;
            }
            default:{
                return new byte[0];
            }
        }
    }

    public byte[] readFile(String fileName){
        try {
            byte[] array = Files.readAllBytes(Path.of(new File(fileName).getPath()));
            return array;
        } catch (IOException e) {
            System.out.println("Не удалось прочитать файл(((");
        }
        return new byte[0];
    }
}
