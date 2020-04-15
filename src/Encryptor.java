import java.util.ArrayList;
import java.util.List;

public class Encryptor {

    //Fields
        private Byte [][] key1;
        private Byte [][] key2;
        private Byte [][] key3;
        private ArrayList<Byte [][]> PlainText;

    public Encryptor() {
    }

    public Encryptor(Byte [][] k1, Byte[][] k2, Byte[][] k3, ArrayList<Byte[][]> message) {
        this.key1 = k1;
        this.key2 = k2;
        this.key3 = k3;
        this.PlainText = message;
    }

    public ArrayList<Byte[][]> encrypt() {
        ArrayList<Byte[][]> CypherText = new ArrayList<>();
        //first iteration
        CypherText = iteration(key1, PlainText);
        //second iteration
        CypherText = iteration(key2, CypherText);
        //third iteration
        CypherText = iteration(key3, CypherText);
        return CypherText;
    }

    /**
     *
     * @param key
     * @return encrypted plain text after 1 iteration (128 bit = 16 byte of 1 block, plaintext is multiply by 16 bytes)
     */
    private ArrayList<Byte[][]> iteration(Byte[][] key, ArrayList<Byte[][]> plain) {
        ArrayList<Byte[][]> encrypted = new ArrayList<>();
        for (Byte[][] block: plain) {
            Byte[][] EncBlock = AddRoundKey(ShiftColumns(block),key);
            encrypted.add(EncBlock);
        }
        return encrypted;
    }

    /**
     *
     * @param block
     * @return block encrypted by shift columns
     * the first column shift up by 0 cell, the second by 1 cell ...
     */
    public Byte[][] ShiftColumns(Byte[][] block){
        Byte[][] shiftBlock = new Byte[4][4];
        //column0
        for (int i = 0; i < 4; i++)
            shiftBlock[i][0]= block[i][0];
        //column1
        for (int i = 1; i < 4 ; i++)
            shiftBlock[i-1][1]= block[i][1];
        shiftBlock[3][1]= block[0][1];
        //column2
        for (int i = 2; i < 4 ; i++)
            shiftBlock[i-2][2]= block[i][2];
        shiftBlock[2][2]= block[0][2];
        shiftBlock[3][2]= block[1][2];
        //column3
        shiftBlock[0][3] = block[3][3];
        for (int i = 1; i < 4 ; i++)
            shiftBlock[i][3] = block[i-1][3];
        return shiftBlock;
    }

    /**
     *
     * @param block of bytes
     * @param key
     * @return
     */
    public static Byte[][] AddRoundKey(Byte[][] block, Byte[][]key){
        Byte[][] blockXorKey = new Byte[4][4];
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                blockXorKey[i][j] = (byte) (block[i][j]^key[i][j]);
        return blockXorKey;
    }

    public void printArr(Byte[][] arr){
        for (int i = 0; i <4 ; i++) {
            for (int j = 0; j < 4; j++)
                System.out.print(arr[i][j] + " |");
            System.out.println();
        }
    }
}
