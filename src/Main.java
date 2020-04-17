import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;


public class Main {

    // Flags for encryption/decryption //
    private static final String Encrypt_Instruction = "-e";
    private static final String Decrypt_Instruction = "-d";
    private static final String Keys_flag = "-k";
    private static final String Input_flag = "-i";
    private static final String Output_flag = "-o";//use for breaking too

    // Flags for breaking //
    private static final String Break_Instruction = "-b";
    private static final String PlainTextPathDenotes = "-m";
    private static final String CypherTextPathDenotes = "-c";

    // Paths //
    private static String Key_Path; // path of the keys to encrypt/decrypte or for output breaking keys file
    private static String InputMassagePath; //path to a file we want to encrypt/decrypt or the plain-text to break
    private static String OutputMassagePath;// path to the output file or the cypher-text we want to break

    // Fields //
    private static Byte[][] Key1 = new Byte[4][4];
    private static Byte[][] Key2 = new Byte[4][4];
    private static Byte[][] Key3 = new Byte[4][4];
    private static ArrayList<Byte[][]> PlainText = new ArrayList<>();
    private static ArrayList<Byte[][]> CypherText = new ArrayList<>();

    public static void main(String[] args) {
        try {
            if (args[1].equals(Keys_flag) && args[3].equals(Input_flag) && args[5].equals(Output_flag)) {
                Key_Path = args[2];
                InputMassagePath = args[4];
                OutputMassagePath = args[6];
                createByteKeys(Key_Path);
                //Encryption
                if (args[0].equals(Encrypt_Instruction)) {
                    readData(InputMassagePath,PlainText);
                    IEncryptor encryptor = new AES_Encryptor(Key1,Key2,Key3,PlainText);
                    CypherText = encryptor.encrypt();
                    writeData(OutputMassagePath,CypherText);
                } else
                //Decryption
                if (args[0].equals(Decrypt_Instruction)) {
                    readData(InputMassagePath, CypherText);
                    IDecryptor decryptor = new AES_Decryptor(Key1,Key2,Key3,CypherText);
                    PlainText = decryptor.decrypt();
                    writeData(OutputMassagePath,PlainText);
                }
            }
            else
            //Breaking
            if (args[0].equals(Break_Instruction) && args[1].equals(PlainTextPathDenotes)
                && args[3].equals(CypherTextPathDenotes) && args[5].equals(Output_flag)) {
                InputMassagePath = args[2];
                OutputMassagePath = args[4];
                Key_Path = args[6];
                readData(InputMassagePath,PlainText);
                readData(OutputMassagePath,CypherText);
                AES_Breaker breaker = new AES_Breaker(PlainText,CypherText);
                //TODO: break method + saving to Key_path
                ArrayList<Byte[][]> key3 = breaker.breakEncryption();
                writeData(Key_Path,key3);
            }
            else //illegal input
                throw new Exception();
        } catch (Exception e) {
            System.out.println("Illegal input parameters, please read the ReadMe.txt file and try again");
        }

    }

    /**
     * Writing data to a new file
     * @param outputMassagePath
     * @param cypherText
     */
    private static void writeData(String outputMassagePath, ArrayList<Byte[][]> cypherText) {
        File file = new File(outputMassagePath);
        byte[] writeable = new byte[cypherText.size()*16];
        int index = 0;
        for (Byte[][] block: cypherText) {
            for (int i = 0; i < block.length; i++) {
                for (int j = 0; j < block[i].length; j++) {
                    writeable[index] = (block[j][i]).byteValue();
                    index++;
                }
            }
        }
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(writeable);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creating the 3 keys according to the key_path.
     * Each key is in size of 128bit = 16 bytes
     * Storing them into the private fields Key1, Key2, Key3 mentioned in the beginning of this code section
     * @param key_path
     */
    private static void createByteKeys(String key_path) {
        File keys = new File(key_path);
        try {
            byte[] massage = Files.readAllBytes(keys.toPath());
            byte[] key1 = new byte[16];
            byte[] key2 = new byte[16];
            byte[] key3 = new byte[16];
            int index = 16;
            for (int i = 0; i < 48; i++) {
                if (i < 16){
                    key1[i] = massage[i];
                }else
                    if (i > 15 && i < 32) {
                        key2[i-index] = massage[i];
                    }else
                        if (i > 31) {
                            key3[i-2*index] = massage[i];
                        }
            }
            buildMatrixData(key1,Key1);
            buildMatrixData(key2,Key2);
            buildMatrixData(key3,Key3);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param arrayData one byte array contains all 16 bytes of the key
     * @param matrixData 4x4 matrix that contains in each cell 1 byte of data
     * The composition of each key is taking each byte from key3 and put it in a column ordered data
     */
    private static void buildMatrixData(byte[] arrayData, Byte[][] matrixData) {
        int index = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                matrixData[j][i] = arrayData[index];
                index++;
            }
        }
    }

    private static void readData(String MassagePath, ArrayList<Byte[][]> plainText) {
        File path = new File(MassagePath);
        try {
            byte[] massage = Files.readAllBytes(path.toPath());
            int numOfBlocks = massage.length/16;
            int index = 0;
            for (int i = 0; i < numOfBlocks; i++) {
                plainText.add(new Byte[4][4]);
//                plainText.set(i,new Byte[4][4]);
                byte[] block = new byte[16];
                for (int j = 0; j < 16; j++) {
                    block[j] = massage[j+index];
                }
                buildMatrixData(block,plainText.get(i));
                index += 16;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
