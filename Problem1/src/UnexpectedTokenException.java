/**
 * @author Glavin Wiechert
 * 
 */
public class UnexpectedTokenException extends Exception
{

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public UnexpectedTokenException(Token receivedToken, String expectedToken)
    {
        super("Expected Protocol to have token " + expectedToken
                + ". Received " + receivedToken + ".");
    }

}
