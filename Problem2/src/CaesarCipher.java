/**
 * Caesar cipher implementation for encrypting and decrypting.
 * 
 * @author Glavin Wiechert
 */
public class CaesarCipher implements Cipher
{

    /**
     * The length of the alphabet used in the Caesar Cipher. English has 26
     * letters, so this value is fixed to 26.
     */
    static final int ALPHABET_SIZE = 26;

    /**
     * The character offset in the Caesar Cipher calculations. Defaults to 3.
     */
    private int charOffset = 3;

    /**
     * Encryption implemented with Caesar cipher.
     */
    public void encrypt(char[] cbuf, int off, int len)
    {
        for (int i = off; i < off + len; i++)
        {
            char c = cbuf[i];
            if (Character.isLetter(c))
            {
                c = (char) ((int) c + charOffset);
                if (!Character.isLetter(c))
                    c = (char) ((int) c - ALPHABET_SIZE);
                // C s no longer in alphabet. Move it back in.
                cbuf[i] = c;
            }
        }
    }

    /**
     * Decryption implemented with Caesar cipher.
     */
    public void decrypt(char[] cbuf, int off, int len)
    {
        for (int i = off; i < off + len; i++)
        {
            char c = cbuf[i];
            if (Character.isLetter(c))
            {
                c = (char) ((int) c - charOffset);
                if (!Character.isLetter(c))
                    c = (char) ((int) c + ALPHABET_SIZE);
                // C is no longer in the alphabet. Move it back in.
                cbuf[i] = c;
            }
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