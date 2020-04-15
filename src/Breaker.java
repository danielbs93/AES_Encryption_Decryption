import com.sun.deploy.util.ArrayUtil;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;

public class Breaker {

    //Fields
    private ArrayList<Byte[][]> PlainText;
    private ArrayList<Byte[][]> CipherText;
    private ArrayList<Byte[][]> Keys;
    private Encryptor encryptor;

    public Breaker(ArrayList<Byte[][]> plainText, ArrayList<Byte[][]> cipherText) {
        this.PlainText = plainText;
        this.CipherText = cipherText;
        this.Keys = new ArrayList<>();
        encryptor = new Encryptor();
    }

    public ArrayList<Byte[][]> breakEncryption() {
        generateTwoKeys();
        Byte[][] Key3 = CipherText.get(0);
        Keys.add(Key3);
        this.encryptor = new Encryptor(Keys.get(0),Keys.get(1),Keys.get(2),PlainText);
        ArrayList<Byte[][]> newCipherText = encryptor.encrypt();
        ArrayList<Byte[][]> originalKey3 = xorCiphers(newCipherText,CipherText);
        Keys.remove(2);
        Keys.addAll(originalKey3);
        return Keys;
    }

    private ArrayList<Byte[][]> xorCiphers(ArrayList<Byte[][]> newCipherText, ArrayList<Byte[][]> cipherText) {
        ArrayList<Byte[][]> ans = new ArrayList<>();
        for (int i = 0; i < newCipherText.size(); i++) {
            Byte[][] xor = Encryptor.AddRoundKey(newCipherText.get(i),CipherText.get(i));
            ans.add(xor);
        }
        return ans;
    }

    private void generateTwoKeys() {
        byte[][] Key1b = new byte[4][4];
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[16]; // 128 bits are converted to 16 bytes;
        for (byte[] row: Key1b) {
            random.nextBytes(row);
        }
//        Key1[1][0] = (byte) 1;
//        for (int i = 0; i < 4; i++) {
//            for (int j = 0; j < 4; j++) {
//                if (i != 1 && j != 0)
//                    Key1[i][j] = (byte) 0;
//            }
//        }
        Byte[][] Key1 = new Byte[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Key1[i][j] = Key1b[i][j];
            }
        }
        Byte[][] Key2 = this.encryptor.ShiftColumns(Key1);
        this.Keys.add(Key1);
        this.Keys.add(Key2);
    }
}
