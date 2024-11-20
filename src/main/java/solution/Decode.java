package solution;

import java.util.*;

public class Decode {
    private Map<String, Byte> byteMap = new HashMap<>();

    public byte[] decode(String fileName, String dicName, String outputImageName){
        IOBytes ioBytes = new IOBytes();
        byte[] dicBytes = ioBytes.readFile(dicName);
        fillByteMap(dicBytes);
        byte[] encodedBytes = ioBytes.readFile(fileName);
        byte[] decodedBytes = new byte[encodedBytes.length/16];
        int counter = 0;

        byte[] originBytes = ioBytes.readFile("test", "jpg");

        for(int i = 0; i < encodedBytes.length - 16; i+=16){
            byte[] ss = Arrays.copyOfRange(encodedBytes, i, i+16);
            try {
                decodedBytes[counter] = byteMap.get(Arrays.toString(ss));
            }catch (Exception e){
                System.out.println(i);
                decodedBytes[counter] = 127;
            }
            counter++;
        }
        ioBytes.writeBytes(decodedBytes, "decoded.jpg", "jpg");
        return decodedBytes;
    }

    private void fillByteMap(byte[] bytes){
        Byte currentByte = -128;
        for(int i = 0; i < bytes.length - 16; i+=16){
            byteMap.put(Arrays.toString(Arrays.copyOfRange(bytes, i, i + 16)), currentByte);
            currentByte++;
        }
    }
}
