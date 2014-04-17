import java.io.Reader;
import java.io.IOException;
import java.nio.CharBuffer;

/**
 * An extended Reader class that uses a Cipher to decrypt the raw text being
 * read. This is an example of the Decorator (or Wrapper) pattern, which
 * enhances functionality without changing the underlying interface and usage.
 * 
 * @author Glavin Wiechert
 */
public class DecryptingReader extends Reader
{

    /**
     * The cipher used.
     */
    private Cipher cipher = new CaesarCipher();

    /**
     * The reader.
     */
    private Reader reader;

    /**
     * Construct an decrypting reader that decorates a given reader
     * 
     * @param reader
     *            The reader to decorate
     */
    public DecryptingReader(Reader reader)
    {
        this.reader = reader;
    }

    /**
     * Attempts to read characters into the specified character buffer. The
     * buffer is used as a repository of characters as-is: the only changes made
     * are the results of a put operation. No flipping or rewinding of the
     * buffer is performed.
     * 
     * @param target
     *            the buffer to read characters into
     * @return The number of characters added to the buffer, or -1 if this
     *         source of characters is at its end
     */
    public int read(CharBuffer target) throws IOException
    {
        return read(target.array());
    }

    /**
     * Reads a single character. This method will block until a character is
     * available, an I/O error occurs, or the end of the stream is reached.
     * Subclasses that intend to support efficient single-character input should
     * override this method.
     * 
     * @return The character read, as an integer in the range 0 to 65535
     *         (0x00-0xffff), or -1 if the end of the stream has been reached
     * @throws IOException
     *             if an I/O error occurs
     */
    public int read() throws IOException
    {
        int result = reader.read();
        if (result == -1)
        {
            return -1;
        } else
        {
            char[] cbuf = { (char) result };
            read(cbuf, 0, 1);
            return cbuf[0];
        }
    }

    /**
     * Reads characters into an array. This method will block until some input
     * is available, an I/O error occurs, or the end of the stream is reached.
     * 
     * @param cbuf
     *            Destination buffer.
     * @return The number of characters read, or -1 if the end of the stream has
     *         been reached
     */
    public int read(char[] cbuf) throws IOException
    {
        return read(cbuf, 0, cbuf.length);
    }

    /**
     * Read the specified characters into a buffer
     * 
     * @param cbuf
     *            Destination buffer.
     * @param off
     *            Offset at which to start storing characters.
     * @param len
     *            Maximum number of characters to read.
     * @return The number of characters read, or -1 if the end of the stream has
     *         been reached
     */
    public int read(char[] cbuf, int off, int len) throws IOException
    {
        int result = reader.read(cbuf, off, len);
        cipher.decrypt(cbuf, off, len);
        return result;
    }

    /**
     * Skips characters. This method will block until some characters are
     * available, an I/O error occurs, or the end of the stream is reached.
     * 
     * @param n
     *            The number of characters to skip
     * @return The number of characters actually skipped
     */
    public long skip(long n) throws IOException
    {
        return reader.skip(n);
    }

    /**
     * Tells whether this stream is ready to be read.
     * 
     * @throws IOException
     *             if an I/O error occurs
     * @return True if the next read() is guaranteed not to block for input,
     *         false otherwise. Note that returning false does not guarantee
     *         that the next read will block.
     */
    public boolean ready() throws IOException
    {
        return reader.ready();
    }

    /**
     * Tells whether this stream supports the mark() operation. The default
     * implementation always returns false. Subclasses should override this
     * method.
     * 
     * @return true if and only if this stream supports the mark operation.
     */
    public boolean markSupported()
    {
        return reader.markSupported();
    }

    /**
     * Marks the present position in the stream. Subsequent calls to reset()
     * will attempt to reposition the stream to this point. Not all
     * character-input streams support the mark() operation.
     * 
     * @param readAheadLimit
     *            Limit on the number of characters that may be read while still
     *            preserving the mark. After reading this many characters,
     *            attempting to reset the stream may fail.
     */
    public void mark(int readAheadLimit) throws IOException
    {
        reader.mark(readAheadLimit);
    }

    /**
     * Resets the stream. If the stream has been marked, then attempt to
     * reposition it at the mark. If the stream has not been marked, then
     * attempt to reset it in some way appropriate to the particular stream, for
     * example by repositioning it to its starting point. Not all
     * character-input streams support the reset() operation, and some support
     * reset() without supporting mark().
     */
    public void reset() throws IOException
    {
        reader.reset();
    }

    /**
     * Closes the stream and releases any system resources associated with it.
     * Once the stream has been closed, further read(), ready(), mark(),
     * reset(), or skip() invocations will throw an IOException. Closing a
     * previously closed stream has no effect.
     */
    public void close() throws IOException
    {
        reader.close();
    }

    /**
     * Get the current cipher.
     * 
     * @return The current cipher.
     */
    public Cipher getCipher()
    {
        return cipher;
    }

    /**
     * Set the cipher to a new strategy.
     * 
     * @param cipher
     *            The new cipher.
     */
    public void setCipher(Cipher cipher)
    {
        this.cipher = cipher;
    }

}