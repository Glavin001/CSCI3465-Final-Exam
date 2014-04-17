/**
 * The interface for encryption and decryption algorithms. *
 * 
 * @author Glavin Wiechert
 */
public interface Cipher {

    /**
     * The encryption algorithm.
     * 
     * @param cbuf
     *            Destination buffer
     * @param off
     *            Offset at which to start storing characters
     * @param len
     *            Maximum number of characters to read
     */
    public void encrypt(char[] cbuf, int off, int len);

    /**
     * The decryption algorithm.
     * 
     * @param cbuf
     *            Destination buffer
     * @param off
     *            Offset at which to start storing characters
     * @param len
     *            Maximum number of characters to read
     */
    public void decrypt(char[] cbuf, int off, int len);

}