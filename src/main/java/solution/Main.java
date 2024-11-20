package solution;

public class Main {
    public static void main(String[] args) {
        Prepare prepare = new Prepare();
        prepare.createDictionary("dic123");
        Encode encode = new Encode();
        encode.encode("dic123", "dic123Encoded");
        encode.encode("testExpended", "testExpendedEncoded");
        Translate translate = new Translate();
        translate.translate("dic123Encoded", "dicTex");
        Decode decode = new Decode();
        decode.decode("testExpendedEncoded", "dic123Encoded", "sdasd");
    }
}
