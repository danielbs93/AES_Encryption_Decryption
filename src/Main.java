import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Main {

    //Flags for encryption/decryption//
    private static final String Encrypt_Instruction = "-e";
    private static final String Decrypt_Instruction = "-d";
    private static final String Keys_flag = "-k";
    private static final String Input_flag = "-i";
    private static final String Output_flag = "-o";

    //Flags for breaking//
    private static final String Break_Instruction = "-b";
    private static final String PlainTextPathDenotes = "-m";
    private static final String CypherTextPathDenotes = "-c";
    private static final String FoundKeysPathDenotes = "-o";

    //Paths
    private static String Key_Path; // path of the keys to encrypt/decrypte or for output breaking keys file
    private static String InputMassagePath; //path to a file we want to encrypt/decrypt or the plain-text to break
    private static String OutputMassagePath;// path to the output file or the cypher-text we want to break


    public static void main(String[] args) {
        try {
            if (args[1].equals(Keys_flag) && args[3].equals(Input_flag) && args[5].equals(Output_flag)) {
                Key_Path = args[2];
                InputMassagePath = args[4];
                OutputMassagePath = args[6];
                readData(Key_Path,InputMassagePath,OutputMassagePath);
                //Encryption
                if (args[0].equals(Encrypt_Instruction)) {

                } else
                //Decryption
                if (args[0].equals(Decrypt_Instruction)) {

                }
            }
            else
            //Breaking
            if (args[0].equals(Break_Instruction)) {

            }
            else //illegal input
                throw new Exception();
        } catch (Exception e) {
            System.out.println("Illegal input parameters, please read the ReadMe.txt file and try again");
        }

    }

    private static void readData(String key_path, String inputMassagePath, String outputMassagePath) {
        File input = new File(inputMassagePath);
        File output = new File(outputMassagePath);
        File keys = new File(key_path);
        try {
            FileReader myReader = new FileReader(input);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
