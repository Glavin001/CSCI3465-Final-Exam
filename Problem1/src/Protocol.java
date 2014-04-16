import java.io.Serializable;

/**
 * Message Protocol that is transmitted between client and server.
 * @author Glavin Wiechert
 */
public class Protocol implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	private Token token;
	
	/**
	 * 
	 */
	private String argument;

	/**
	 * Constructor
	 */
	public Protocol(Token t, String a) {
		this.token = t;
		this.argument = a;
	}
	
	/**
	 * @return the token
	 */
	public Token getToken() {
		return token;
	}

	/**
	 * @return the argument
	 */
	public String getArgument() {
		return argument;
	}

	
}
