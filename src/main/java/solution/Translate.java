package solution;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Translate {

    /**
     * Создает текстовый документ с байтами dict
     * @param dicName
     * @param fileName
     */
    public List<String> translate(String dicName, String fileName){
        IOBytes ioBytes = new IOBytes();
        byte[] encodedDicBytes = ioBytes.readFile(dicName);
        List<String> inLineBytes = new ArrayList<>();
        byte basicByte = -128;
        for(int i = 0; i < encodedDicBytes.length; i+=16){
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("byte: ");
            stringBuilder.append(String.valueOf(basicByte));
            stringBuilder.append(" [ ");
            for(int j = i; j < 16+i; j++){
                stringBuilder.append(String.valueOf(encodedDicBytes[j]));
                stringBuilder.append(" ");
            }
            stringBuilder.append("]");
            inLineBytes.add(stringBuilder.toString());
            basicByte++;
        }

        try(FileWriter fileWriter = new FileWriter(fileName);) {
            for (String str : inLineBytes) {
                fileWriter.write(str + System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return inLineBytes;
    }

    /**
     * public List<String> translate(String dicName, String fileName){
     *         IOBytes ioBytes = new IOBytes();
     *         byte[] encodedDicBytes = ioBytes.readFile(dicName);
     *         List<String> inLineBytes = new ArrayList<>();
     *         for(int i = 0; i < encodedDicBytes.length; i+=16){
     *             StringBuilder stringBuilder = new StringBuilder();
     *             stringBuilder.append("byte: ");
     *             stringBuilder.append(String.valueOf(encodedDicBytes[i]));
     *             stringBuilder.append(" [ ");
     *             for(int j = i+1; j < 16+i; j++){
     *                 stringBuilder.append(String.valueOf(encodedDicBytes[j]));
     *                 stringBuilder.append(" ");
     *             }
     *             stringBuilder.append("]");
     *             inLineBytes.add(stringBuilder.toString());
     *         }
     *
     *         try(FileWriter fileWriter = new FileWriter(fileName);) {
     *             for (String str : inLineBytes) {
     *                 fileWriter.write(str + System.lineSeparator());
     *             }
     *         } catch (IOException e) {
     *             throw new RuntimeException(e);
     *         }
     *         return inLineBytes;
     *     }
     */
}
