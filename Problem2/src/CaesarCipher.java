/**
 * Caesar cipher implementation for encrypting and decrypting.
 * 
 * @author Glavin Wiechert
 */
public class CaesarCipher implements Cipher
{

    /**
     * The length of the alphabet used in the Caesar Cipher. The char data type
     * is a single 16-bit Unicode character. It has a minimum value of '\u0000'
     * (or 0) and a maximum value of '\uffff' (or 65,535 inclusive).
     */
    static final int ALPHABET_SIZE = 65535;

    /**
     * The character offset in the Caesar Cipher calculations. Defaults to 3.
     */
    private int charOffset = 3;

    /**
     * Encryption implemented with Caesar cipher.
     */
    public void encrypt(char[] cbuf, int off, int len)
    {
        // Iterate over all characters
        for (int i = off; i < off + len; i++)
        {
            char c = cbuf[i];
            int c2 = (int) c + charOffset;
            if (c2 > ALPHABET_SIZE)
            {
                c2 = c2 - ALPHABET_SIZE;
            }
            cbuf[i] = (char) (c2);
        }
    }

    /**
     * Decryption implemented with Caesar cipher.
     */
    public void decrypt(char[] cbuf, int off, int len)
    {
        // Iterate over all characters
        for (int i = off; i < off + len; i++)
        {
            char c = cbuf[i];
            int c2 = (int) c - charOffset;
            if (c2 < 0)
            {
                c2 = ALPHABET_SIZE + c2;
            }
            cbuf[i] = (char) (c2);
        }
    }

    /**
     * Get the current character offset.
     * 
     * @return the charOffset
     */
    public int getCharOffset()
    {
        return charOffset;
    }

    /**
     * Set the character offset.
     * 
     * @param charOffset
     *            the charOffset to set
     */
    public void setCharOffset(int charOffset)
    {
        this.charOffset = charOffset;
    }

}