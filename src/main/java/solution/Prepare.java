package solution;

public class Prepare {
    private final IOBytes ioBytes = new IOBytes();

    /**
     * Создает словарь байтов
     * @param fileName
     */
    public byte[] createDictionary(String fileName){
        byte[] byteArr = new byte[256+256*15-16];
        byte byteValue = -128;
        for(int i = 0; i < byteArr.length; i+=16){
            //byteArr[i] = (byte) i;
            byteArr[i] = byteValue;
            byteValue++;
        }

        /**
         * If no registered ImageReader claims to be able to read the resulting stream, null is returned.
         *
         * Writing random bytes to an array is not going to create valid image data in any format, because image formats have a standard structure that consists, at the very least, of a header. For this reason, ImageIO.read returns null.
         */
        //ioBytes.writeBytes(byteArr, fileName, "jpg");
        ioBytes.writeBytes(byteArr, fileName, "randomFormat");
        return byteArr;
    }

    /**
     * Расширить файл нулевыми байтами, для AES n=16, кол-во нулей между ненулевыми байтами n-1
     * @param fileName
     */
    public byte[] expendFile(String fileName){
        byte[] bytes = ioBytes.readFile(fileName, "jpg");
        byte[] expendBytes = new byte[bytes.length+bytes.length*15];
        int counter = 0;
        for(int i = 0; i < expendBytes.length; i+=16){
            expendBytes[i] = bytes[counter];
            counter++;
        }
        ioBytes.writeBytes(expendBytes, fileName+"Expended", "randomFormat");
        return expendBytes;
    }

}
