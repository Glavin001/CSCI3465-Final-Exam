import java.io.Writer;
import java.io.IOException;

/**
 * A decorator class for encrypting text before writing it.
 * 
 * @author Glavin Wiechert
 */
public class EncryptingWriter extends Writer {

    /**
     * The cipher used.
     */
    private Cipher cipher = new CaesarCipher();

    /**
     * The writer.
     */
    private Writer writer;

    /**
     * Construct an encrypting writer that decorates a given writer
     * 
     * @param writer
     *            The writer to decorate
     */
    public EncryptingWriter(Writer writer) {
        this.writer = writer;
    }

    /**
     * Writes a single character. The character to be written is contained in
     * the 16 low-order bits of the given integer value; the 16 high-order bits
     * are ignored. Subclasses that intend to support efficient single-character
     * output should override this method.
     * 
     * @param c
     *            int specifying a character to be written
     */
    public void write(int c) throws IOException {
        char[] cbuf = { (char) c };
        write(cbuf);
    }

    /**
     * Writes an array of characters.
     * 
     * @param cbuf
     *            Array of characters to be written
     */
    public void write(char[] cbuf) throws IOException {
        write(cbuf, 0, cbuf.length);
    }

    /**
     * Write the specified characters from a buffer
     * 
     * @param cbuf
     *            Destination buffer.
     * @param off
     *            Offset at which to start storing characters.
     * @param len
     *            Maximum number of characters to read.
     */
    public void write(char[] cbuf, int off, int len) throws IOException {
        //
        cipher.encrypt(cbuf, off, len);
        // Output the result
        writer.write(cbuf, off, len);
    }

    /**
     * Writes a string.
     * 
     * @param str
     *            String to be written
     */
    public void write(String str) throws IOException {
        write(str.toCharArray());
    }

    /**
     * Writes a portion of a string.
     * 
     * @param str
     *            A String
     * @param off
     *            Offset from which to start writing characters
     * @param len
     *            Number of characters to write
     */
    public void write(String str, int off, int len) throws IOException {
        write(str.toCharArray(), off, len);
    }

    /**
     * Appends the specified character sequence to this writer. An invocation of
     * this method of the form out.append(csq) behaves in exactly the same way
     * as the invocation
     * 
     * out.write(csq.toString()) Depending on the specification of toString for
     * the character sequence csq, the entire sequence may not be appended. For
     * instance, invoking the toString method of a character buffer will return
     * a subsequence whose content depends upon the buffer's position and limit.
     * 
     * @param csq
     *            The character sequence to append. If csq is null, then the
     *            four characters "null" are appended to this writer.
     * @return This writer
     */
    public Writer append(CharSequence csq) throws IOException {
        write(csq.toString());
        return writer;
    }

    /**
     * Appends a subsequence of the specified character sequence to this writer.
     * Appendable. An invocation of this method of the form out.append(csq,
     * start, end) when csq is not null behaves in exactly the same way as the
     * invocation
     * 
     * out.write(csq.subSequence(start, end).toString())
     * 
     * @param csq
     *            The character sequence from which a subsequence will be
     *            appended. If csq is null, then characters will be appended as
     *            if csq contained the four characters "null".
     * @param start
     *            The index of the first character in the subsequence
     * @param end
     *            The index of the character following the last character in the
     *            subsequence
     * @return This writer
     */
    public Writer append(CharSequence csq, int start, int end)
            throws IOException {
        write(csq.subSequence(start, end).toString());
        return writer;
    }

    /**
     * Appends the specified character to this writer. An invocation of this
     * method of the form out.append(c) behaves in exactly the same way as the
     * invocation
     * 
     * out.write(c)
     * 
     * @param c
     *            The 16-bit character to append
     * @return This writer
     */
    public Writer append(char c) throws IOException {
        write(c);
        return writer;
    }

    /**
     * Flushes the stream. If the stream has saved any characters from the
     * various write() methods in a buffer, write them immediately to their
     * intended destination. Then, if that destination is another character or
     * byte stream, flush it. Thus one flush() invocation will flush all the
     * buffers in a chain of Writers and OutputStreams. If the intended
     * destination of this stream is an abstraction provided by the underlying
     * operating system, for example a file, then flushing the stream guarantees
     * only that bytes previously written to the stream are passed to the
     * operating system for writing; it does not guarantee that they are
     * actually written to a physical device such as a disk drive.
     */
    public void flush() throws IOException {
        writer.flush();
    }

    /**
     * Closes the stream, flushing it first. Once the stream has been closed,
     * further write() or flush() invocations will cause an IOException to be
     * thrown. Closing a previously closed stream has no effect.
     */
    public void close() throws IOException {
        writer.close();
    }

    /**
     * Get the current cipher.
     * 
     * @return The current cipher.
     */
    public Cipher getCipher() {
        return cipher;
    }

    /**
     * Set the cipher to a new strategy.
     * 
     * @param cipher
     *            The new cipher.
     */
    public void setCipher(Cipher cipher) {
        this.cipher = cipher;
    }

}