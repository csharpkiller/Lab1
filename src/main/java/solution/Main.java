package solution;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Prepare prepare = new Prepare();
        //prepare.createDictionary("dic123");
        //prepare.expendFile("test");
        Encode encode = new Encode();
        //encode.encode("dic123", "dic123Encoded");
        //encode.encode("testExpended", "testExpendedEncoded");
        Translate translate = new Translate();
        //translate.translate("dic123Encoded", "dicTex");
        Decode decode = new Decode();
        //decode.decode("testExpendedEncoded", "dic123Encoded", "sdasd");

        while(true){
            Scanner scanner = new Scanner(System.in);
            String readLine = scanner.nextLine();
            String[] input = readLine.split(" ");
            switch (input[0]){
                case "prepare":{
                    prepare.createDictionary("dic");
                    prepare.expendFile(input[1]);
                    System.out.println("PacwuPeJl u Co3DaJl CJlOBAPb :)");
                }
                break;
                case "encode":{
                    encode.encode("dic", "dicEncoded");
                    encode.encode(input[1]+"Expended", input[1]+"Expended"+"Encoded");
                    System.out.println("3AKODUPOBAJl 6A6YWKY GAGAGA");
                }
                break;
                case "translate":{
                    translate.translate("dicEncoded", "dicTale");
                    System.out.println("CjloBaPuK Created");
                }
                break;
                case "decode":{
                    decode.decode(input[1]+"Expended"+"Encoded", "dicEncoded", input[1]+"decoded");
                    System.out.println("6a6yWkA B IIOP9DkE!!! 0_o");
                }
                break;
                case "auto": {
                    prepare.createDictionary("dic");
                    prepare.expendFile(input[1]);
                    System.out.println("PacwuPeJl u Co3DaJl CJlOBAPb :)");
                    encode.encode("dic", "dicEncoded");
                    encode.encode(input[1]+"Expended", input[1]+"Expended"+"Encoded");
                    System.out.println("3AKODUPOBAJl 6A6YWKY GAGAGA");
                    translate.translate("dicEncoded", "dicTale");
                    System.out.println("CjloBaPuK Created");
                    decode.decode(input[1]+"Expended"+"Encoded", "dicEncoded", input[1]+"decoded");
                    System.out.println("6a6yWkA B IIOP9DkE!!! 0_o");
                }
                break;
                case "exit":
                    return;
            }
        }
    }
}
