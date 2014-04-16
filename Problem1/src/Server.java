import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * @author Glavin Wiechert
 *
 */
public class Server 
{

	/**
	 * Server socket.
	 */
	private ServerSocket serverSocket;
	
	/**
	 * Client connected socket.
	 */
	private Socket socket;
	
	/**
	 * 
	 */
	private ObjectOutputStream output;
	
	/**
	 * 
	 */
	private ObjectInputStream input;
	
	/**
	 * 
	 */
	private boolean isListening;
	
	/**
	 * Constructor
	 */
	public Server() 
	{
		this.isListening = false;
	}
	
	/**
	 * Start the server, listening on the given port for client connections.
	 * @param port Port to listen on.
	 */
	public void listen(int port) 
	{
		try {
			// Start listening
			this.serverSocket = new ServerSocket(port);	
	        System.out.println("Listening on port "+port+".");
			this.isListening = true;
			
			// Wait for connections
			while (isListening) 
			{
				// Get a Client Connection
				this.socket = this.serverSocket.accept();
                System.out.println("Found new connection.");
                
                // Get Object Streams 
                this.output = new ObjectOutputStream(this.socket.getOutputStream());
                this.input = new ObjectInputStream(this.socket.getInputStream());
                
            	try {
	                // Wait for CONNECT message
	                Protocol p = (Protocol) input.readObject();
	                if (p.getToken().equals(Token.CONNECT)) {
	                	
	                	// Process Loop
	                	while (socket.isConnected())
	                	{
		                	
		                	// Tell Client that Server is ready. 
		                	this.output.writeObject(new Protocol(Token.READY, null));
		                	System.out.println("Ready for command.");
		                	
		                	// Wait for Arithmetic Operation or QUIT
		                	p = (Protocol) input.readObject();
		                	// Perform respective operation
		                	switch (p.getToken()) {
		                		//Arithmetic Operations
			                	case PLUS: 
			                	case MINUS: 
			                	case MULTIPLY: 
			                	case DIVIDE: 
				                {
				                	// Read two CONSTs
				                	Protocol c1 = (Protocol) this.input.readObject();
				                	Protocol c2 = (Protocol) this.input.readObject();
				                	
				                	// Validate CONSTs
				                	Double v1;
				                	Double v2;
				                	try {
					                	v1 = Double.parseDouble( c1.getArgument() );
					                	v2 = Double.parseDouble( c2.getArgument() );
				                	} catch (NumberFormatException e) {
				                		throw new UnexpectedArgumentException("Argument(s) could not be parsed as doubles.");
				                	}
				                	
				                	// Perform operation
				                	Double result;
				                	switch (p.getToken()) {
					                	case PLUS:
					                	{
					                		result = v1 + v2;
					                	}
					                	break;
					                	case MINUS:
					                	{
					                		result = v1 - v2;
					                	}
					                	break;
					                	case MULTIPLY:
					                	{
					                		result = v1 * v2;
					                	}
					                	break;
					                	case DIVIDE:
					                	{
					                		result = v1 / v2;
					                	}
					                	break;
					                	default: 
					                	{
				                			// Unexpected operation token.
				    						throw new UnexpectedTokenException(p.getToken(), "PLUS or MINUS or DIVIDE or MULTIPLY"); 								                		
					                	}
				                	}
				                	
				                	// Send back result
				                	this.output.writeObject(new Protocol(Token.CONST2, String.valueOf(result)));
				                	
				                	// Continue, and loop to process next operation.
				                	
				                }
			                	break;
			                	case QUIT: 
				                {
									// Send QUIT
									this.output.writeObject(new Protocol(Token.QUIT, "QUIT"));
									// Close connection
									this.output.close();
									this.input.close();
									this.socket.close();
				                }
			                	break;
		                		default: 
		                		{
		                			// Unexpected operation token.
		    						throw new UnexpectedTokenException(p.getToken(), "PLUS or MINUS or DIVIDE or MULTIPLY or QUIT");	                			
		                		}
		                	}
		                	
	                	}
	                } else {
						throw new UnexpectedTokenException(p.getToken(), "CONNECT");
	                }
				} catch (UnexpectedTokenException e) {
					// Send ERROR
					this.output.writeObject(new Protocol(Token.ERROR, e.getMessage()));
					// Close connection
					this.output.close();
					this.input.close();
					this.socket.close();
					// 
					e.printStackTrace();
				}
				catch (UnexpectedArgumentException e) {
					// Send ERROR
					this.output.writeObject(new Protocol(Token.ERROR, e.getMessage()));
					// Close connection
					this.output.close();
					this.input.close();
					this.socket.close();
					// 
					e.printStackTrace();
				}
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		
	}

}
