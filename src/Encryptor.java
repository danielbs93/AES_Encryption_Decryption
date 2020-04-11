public class Encryptor {

    //Fields
        private static Byte [][] key1;
        private static Byte [][] key2;
        private static Byte [][] key3;
//        private static Byte [][] PlainText;

    public Encryptor(Byte [][] k1, Byte[][] k2, Byte[][] k3, Byte[][] message) {
        this.key1 = k1;
        this.key2 = k2;
        this.key3 = k3;
//        this.PlainText = message;
    }

    /**
     *
     * @param block
     * @return block encrypted by shift columns
     * the first column shift up by 0 cell, the second by 1 cell ...
     */
    private Byte[][] ShiftColumns(Byte[][] block){
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
    private Byte[][] AddRoundKey(Byte[][] block, Byte[][]key){
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
