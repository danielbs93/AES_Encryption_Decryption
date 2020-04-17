import com.sun.deploy.util.ArrayUtil;
import javafx.application.Platform;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;

public class Breaker {

    //Fields
    private ArrayList<Byte[][]> PlainText;
    private ArrayList<Byte[][]> CipherText;
    private ArrayList<Byte[][]> RandomKeys;
    private Encryptor encryptor;

    public Breaker(ArrayList<Byte[][]> plainText, ArrayList<Byte[][]> cipherText) {
        this.PlainText = plainText;
        this.CipherText = cipherText;
        this.RandomKeys = new ArrayList<>();
        encryptor = new Encryptor();
    }

    public ArrayList<Byte[][]> breakEncryption() {
        generateTwoRandomKeys();
        ArrayList<Byte[][]> FinalKeys = new ArrayList<>();
        FinalKeys.add(RandomKeys.get(0));
        FinalKeys.add(RandomKeys.get(1));
        Byte[][] Key3 = CipherText.get(0);
        RandomKeys.add(Key3);
        ArrayList<Byte[][]> xorPlainText = PlainText;
        xorPlainText = shiftColumnsThreeTimes(PlainText);
        ArrayList<Byte[][]> originalKey3 = xorCiphers(xorPlainText,CipherText);
        FinalKeys.addAll(originalKey3);
        return FinalKeys;
    }

    private ArrayList<Byte[][]> shiftColumnsThreeTimes(ArrayList<Byte[][]> plaintext) {
        ArrayList<Byte[][]> result = plaintext;
        for (int i = 0; i < 3; i++) {
            ArrayList<Byte[][]> tempPlainText = new ArrayList<>();
            for (Byte[][] block: result) {
                tempPlainText.add(this.encryptor.ShiftColumns(block));
            }
            result = tempPlainText;
        }
        return result;
    }

    /**
     *
     * @param newCipherText
     * @param cipherText
     * @return array list of key while xor is activated between each Byte[]
     */
    private ArrayList<Byte[][]> xorCiphers(ArrayList<Byte[][]> newCipherText, ArrayList<Byte[][]> cipherText) {
        ArrayList<Byte[][]> ans = new ArrayList<>();
        for (int i = 0; i < newCipherText.size(); i++) {
            Byte[][] xor = Encryptor.AddRoundKey(newCipherText.get(i),CipherText.get(i));
            ans.add(xor);
        }
        return ans;
    }

    /**
     * Generate 2 keys that will cancel each other when activating the aes3
     */
    private void generateTwoRandomKeys() {
        byte[][] Key1b = new byte[4][4];
        SecureRandom random = new SecureRandom();
        for (byte[] row: Key1b) {
            random.nextBytes(row);
        }
        Byte[][] Key1 = new Byte[4][4];
        Byte[][] Key2 = new Byte[4][4];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                    Key1[i][j] = 0;
                    Key2[i][j] = 0;
            }
        }
        Key1[0][1] = 1;
        Key2[3][1] = 1;// simulate shift columns
        this.RandomKeys.add(Key1);
        this.RandomKeys.add(Key2);
    }
}
