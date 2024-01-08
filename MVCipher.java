import java.io.PrintWriter;
import java.util.Scanner;

/**
 *  MVCipher - Program to Encrypt or Decrypt depending on user choice
 *  Integrated with Prompt and FileUtils classes
 *
 *  @author Prathik Kumar
 *  @since  September 22, 2023
 */
public class MVCipher {
    private String keyWord;
    private int menu;

    private String inputFile;
    private String outputFile;
    private int index;

    /** Constructor */
    public MVCipher() {
        index = 0;
        inputFile = "";
        outputFile = "";
    }

    public static void main(String[] args) {
        MVCipher mvc = new MVCipher();
        mvc.run();
    }

    /**
     *  This method has the user interaction and calls the other methods
     *  used for this program.
     */
    public void run() {
        System.out.println("\n Welcome to the MV Cipher machine!\n");

        keyWord = Prompt.getString("Please input a word to use as key (letters only)").toUpperCase();
        while (keyWord.length() <=2 || !validKeywordCheck(keyWord)) {
            keyWord = Prompt.getString("ERROR: Key must be all letters and at least 3 characters long").toUpperCase();
        }

        menu = Prompt.getInt("Encrypt or decrypt? (1, 2) ");
        while (menu!=1 && menu!=2) {
            menu = Prompt.getInt("Encrypt or decrypt? (1, 2) ");
        }
        System.out.println();

        if (menu == 1) {
            inputFile = Prompt.getString("Name of file to encrypt ");
            outputFile = Prompt.getString("Name of output file ");
            encryptDecryptLogic(inputFile, outputFile, menu);
            System.out.println("The encrypted file " + outputFile + " has been created using the keyword -> " + keyWord.toUpperCase());
        }

        if (menu == 2) {
            inputFile = Prompt.getString("Name of file to decrypt ");
            outputFile = Prompt.getString("Name of output file ");
            encryptDecryptLogic(inputFile, outputFile, menu);
            System.out.println("The decrypted file " + outputFile + " has been created using the keyword -> " + keyWord.toUpperCase());
        }
    }

    /**
     * Checks if a valid keyword entered by the user
     *
     * @param input         user entered keyWord for either encryption or decyption
     * @return {returns true} if the input string is a valid keyword, {returns false} otherwise.
     */
    public static boolean validKeywordCheck(String input) {
        for (int i = 0; i < input.length(); ++i) {
            if (input.charAt(i) < 'A' || input.charAt(i) > 'Z') {
                return false;
            }
        }
        return true;
    }

    /**
     * Performs encryption or decryption based on the menu option and writes to output file.
     *
     * @param inputFile       The name of the input file to be processed.
     * @param outputFile      The name of the output file where the result will be written.
     * @param menu            The menu option indicating whether to encrypt (1) or decrypt (2).
     */
    public void encryptDecryptLogic(String inputFile, String outputFile, int menu) {
        Scanner fileScanner = FileUtils.openToRead(inputFile);
        PrintWriter printWriter = openFileForWrite(outputFile);
        while (fileScanner.hasNext()) {
            String fileConent = fileScanner.nextLine();
            String fileOutput ="";
            int keyLen = keyWord.length();
            for (int i = 0; i < fileConent.length(); ++i) {
                char enDeChar = fileConent.charAt(i);
                char currentChar = fileConent.charAt(i);
                char currentkeyChar = keyWord.charAt(index);
                if (Character.isSpaceChar(currentChar) || !Character.isLetter(currentChar)) {
                    enDeChar = currentChar;
                }
                else {
                    if (index < keyLen-1 )
                        index++;
                    else
                        index = 0;
                    if (Character.isLowerCase(currentChar)) {
                        enDeChar = lowerCaseEnDe(currentkeyChar, enDeChar, currentChar, menu);
                    }
                    if (Character.isUpperCase(currentChar)) {
                        enDeChar = upperCaseEnDe(currentkeyChar, enDeChar, currentChar, menu);
                    }
                }
                fileOutput += enDeChar;
            }
            writeToFile(printWriter, fileOutput);
        }
        fileScanner.close();
        closeFile(printWriter);
    }

    /**
     * Encrypts or decrypts a lowercase character based on the provided key character's index position.
     * This method handles lower case encryption or decryption and
     * returns the encrypted or decrypted character based on the key provided.
     *
     * @param currentkeyChar    The key character used for encryption or decryption.
     * @param enDeChar          The character to be encrypted or decrypted.
     * @param currentChar       The original character before encryption or decryption.
     * @param menu              The menu option indicating whether to encrypt (1) or decrypt (2).
     * @return                  The encrypted or decrypted character.
     */
    public char lowerCaseEnDe(char currentkeyChar, char enDeChar, char currentChar, int menu) {
        int shiftKey = currentkeyChar - 'A' + 1;
        if (menu == 1) {
            enDeChar = (char)(currentChar + shiftKey);

            if (enDeChar > 'z') {
                enDeChar -= 26;
            }
        }
        else if (menu == 2) {
            enDeChar = (char)(currentChar - shiftKey);
            if (enDeChar < 'a') {
                enDeChar += 26;
            }
        }
        return enDeChar;
    }

    /**
     * Encrypts or decrypts a uppercase character based on the provided key character's index position.
     * This method handles lower case encryption or decryption and
     * returns the encrypted or decrypted character based on the key provided.
     *
     * @param currentkeyChar    The key character used for encryption or decryption.
     * @param enDeChar          The character to be encrypted or decrypted.
     * @param currentChar       The original character before encryption or decryption.
     * @param menu              The menu option indicating whether to encrypt (1) or decrypt (2).
     * @return                  The encrypted or decrypted character.
     */
    public char upperCaseEnDe(char currentkeyChar, char enDeChar, char currentChar, int menu) {
        int shiftKey = currentkeyChar - 'A' + 1;
        if (menu == 1) {
            enDeChar = (char)(currentChar + shiftKey);
            if (enDeChar > 'Z') {
                enDeChar -= 26;
            }
        }
        else if (menu == 2) {
            enDeChar = (char)(currentChar - shiftKey);
            if (enDeChar < 'A') {
                enDeChar += 26;
            }
        }
        return enDeChar;
    }

    /**
     * Opens a file for writing and returns a PrintWriter for the specified output file.
     *
     * @param outputFile The name of the output file to be opened for writing.
     * @return A PrintWriter object ready to write data to the specified output file.
     */
    public PrintWriter openFileForWrite(String outputFile) {
        return FileUtils.openToWrite(outputFile);
    }
    
    /**
     * Writes the provided content to the specified PrintWriter and appends a new line.
     *
     * @param printWriter The PrintWriter to which the content should be written.
     * @param content     The content to be written to the PrintWriter.
     */
    public void writeToFile(PrintWriter printWriter, String content ) {
        printWriter.println(content);
    }

    /**
     * Closes the file after wrting using print writer
     * @param printWriter
     */
    public void closeFile(PrintWriter printWriter) {
        printWriter.close();
    }
}
