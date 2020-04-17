import java.util.ArrayList;

public class AES_Decryptor  implements IDecryptor{

    //Fields
    private Byte [][] key1;
    private Byte [][] key2;
    private Byte [][] key3;
    private ArrayList<Byte [][]> CypherText;

    public AES_Decryptor(Byte [][] k1, Byte[][] k2, Byte[][] k3, ArrayList<Byte[][]> message) {
        this.key1 = k1;
        this.key2 = k2;
        this.key3 = k3;
        this.CypherText = message;

    }

    public ArrayList<Byte[][]> decrypt() {
        ArrayList<Byte[][]> PlainText = new ArrayList<>();
        //first iteration
        PlainText = iteration(key3, CypherText);
        //second iteration
        PlainText = iteration(key2, PlainText);
        //third iteration
        PlainText = iteration(key1, PlainText);
        return PlainText;
    }

    /**
     *
     * @param key
     * @return decrypted cypher text after 1 iteration (128 bit = 16 byte of 1 block, cyphertext is multiply by 16 bytes)
     */
    private ArrayList<Byte[][]> iteration(Byte[][] key, ArrayList<Byte[][]> plain) {
        ArrayList<Byte[][]> decrypted = new ArrayList<>();
        for (Byte[][] block: plain) {
            Byte[][] EncBlock = InvShiftColumns(AES_Encryptor.AddRoundKey(block,key));
            decrypted.add(EncBlock);
        }
        return decrypted;
    }
    /**
     *
     * @param shiftBlock
     * @return block encrypted by shift columns
     * the first column shift up by 0 cell, the second by 1 cell ...
     */
    private Byte[][] InvShiftColumns(Byte[][] shiftBlock){
        Byte[][] block = new Byte[4][4];
        //column0
        for (int i = 0; i < 4; i++)
            block[i][0]= shiftBlock[i][0];
        //column1
        block[0][1]= shiftBlock[3][1];
        for (int i = 1; i < 4 ; i++)
            block[i][1]= shiftBlock[i-1][1];
        //column2
        for (int i = 2; i < 4 ; i++)
            block[i][2]= shiftBlock[i-2][2];
        block[0][2]= shiftBlock[2][2];
        block[1][2]= shiftBlock[3][2];
        //column3
        block[3][3] = shiftBlock[0][3];
        for (int i = 1; i < 4 ; i++)
            block[i-1][3] = shiftBlock[i][3];
        return block;
    }
}
