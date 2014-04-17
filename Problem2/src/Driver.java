import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.BufferedReader;

/**
 * Tests the EncryptingWriter and DecryptingReader classes.
 * 
 * @author Glavin Wiechert
 */
class Driver
{

    public static void main(String[] args) throws IOException
    {
        String testStr1 = "abcdefghijklmnopqrstuvwxyz";
        String testStr2 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String filePath = "test.out";
        // Create Cipher strategies
        CaesarCipher cipher1 = new CaesarCipher();
        cipher1.setCharOffset(3); // Default is 3

        CaesarCipher cipher2 = new CaesarCipher();
        cipher2.setCharOffset(12345); // Default is 3

        System.out.println("=== Original Text ===");
        System.out.println("1. " + testStr1);
        System.out.println("2. " + testStr2);

        EncryptingWriter e = new EncryptingWriter(new FileWriter(filePath));
        // ENHANCED: Change the Strategy, to a custom CaesarCipher.
        e.setCipher(cipher1);

        // Write to file with cipher1
        // System.out.println("=== Writing with Cipher 1 ===");
        e.write(testStr1, 0, testStr1.length());
        e.write(testStr2, 0, testStr2.length());

        // Change Cipher
        e.setCipher(cipher2);
        // Write to file with cipher2
        // System.out.println("=== Writing with Cipher 2 ===");
        e.write(testStr1, 0, testStr1.length());
        e.write(testStr2, 0, testStr2.length());

        // Flush and write to file.
        e.flush();

        System.out.println();

        // Read
        FileReader fr = new FileReader(filePath);

        char inChars[] = new char[testStr1.length()];
        System.out.println("=== Reading Encrypted Text from Cipher 1 ===");
        fr.read(inChars);
        System.out.println("1. " + new String(inChars));
        fr.read(inChars);
        System.out.println("2. " + new String(inChars));

        System.out.println("=== Reading Encrypted Text from Cipher 2 ===");
        fr.read(inChars);
        System.out.println("1. " + new String(inChars));
        fr.read(inChars);
        System.out.println("2. " + new String(inChars));

        // Reset
        fr.close();
        fr = new FileReader(filePath);

        System.out.println();
        System.out.println("=== Preparing to Decrypt ===");
        DecryptingReader r = new DecryptingReader(fr);
        // ENHANCED: Change the Strategy, to a custom CaesarCipher.
        r.setCipher(cipher1);

        //
        System.out.println("=== Reading with Cipher 1 ===");
        r.read(inChars);
        System.out.println("1. "
                + new String(inChars)
                + (testStr1.equals(new String(inChars)) ? " <== Correct"
                        : "Incorrect"));

        r.read(inChars);
        System.out.println("2. "
                + new String(inChars)
                + (testStr2.equals(new String(inChars)) ? " <== Correct"
                        : "Incorrect"));

        // Change Cipher
        r.setCipher(cipher2);
        System.out.println("=== Reading with Cipher 2 ===");
        r.read(inChars);
        System.out.println("1. "
                + new String(inChars)
                + (testStr1.equals(new String(inChars)) ? " <== Correct"
                        : "Incorrect"));
        r.read(inChars);
        System.out.println("2. "
                + new String(inChars)
                + (testStr2.equals(new String(inChars)) ? " <== Correct"
                        : "Incorrect"));

    }
}