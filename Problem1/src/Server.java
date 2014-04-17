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
     * 
     * @param port
     *            Port to listen on.
     */
    public void listen(int port)
    {
        // Start listening
        try
        {
            this.serverSocket = new ServerSocket(port);
            System.out.println("Listening on port " + port + ".");
            this.isListening = true;

            // Wait for connections
            while (isListening)
            {

                try
                {
                    // Get a Client Connection
                    this.socket = this.serverSocket.accept();
                    System.out.println("Found new connection.");

                    // Get Object Streams
                    this.output = new ObjectOutputStream(
                            this.socket.getOutputStream());
                    this.input = new ObjectInputStream(
                            this.socket.getInputStream());

                    try
                    {
                        // Wait for CONNECT message
                        Protocol p = (Protocol) input.readObject();
                        if (p.getToken().equals(Token.CONNECT))
                        {

                            // Process Loop
                            while (!socket.isClosed()
                                    && !socket.isOutputShutdown()
                                    && !socket.isInputShutdown())
                            {

                                // Tell Client that Server is ready.
                                this.output.writeObject(new Protocol(
                                        Token.READY, null));
                                System.out.println("Ready for command.");

                                // Wait for Arithmetic Operation or QUIT
                                System.out.println("Wait for operation.");
                                p = (Protocol) input.readObject();
                                // Perform respective operation
                                switch (p.getToken())
                                {
                                // Arithmetic Operations
                                case PLUS:
                                case MINUS:
                                case MULTIPLY:
                                case DIVIDE:
                                {
                                    // Read two CONSTs
                                    Protocol c1 = (Protocol) this.input
                                            .readObject();
                                    Protocol c2 = (Protocol) this.input
                                            .readObject();

                                    // Validate CONSTs
                                    Double v1;
                                    Double v2;
                                    if (c1.getToken().equals(Token.CONST1)
                                            && c1.getToken().equals(
                                                    Token.CONST1))
                                    {
                                        try
                                        {
                                            v1 = Double.parseDouble(c1
                                                    .getArgument());
                                            v2 = Double.parseDouble(c2
                                                    .getArgument());
                                        } catch (NumberFormatException e)
                                        {
                                            throw new UnexpectedArgumentException(
                                                    "Argument(s) could not be parsed as doubles.");
                                        }

                                    } else
                                    {
                                        // Unexpected operation token.
                                        throw new UnexpectedTokenException(
                                                p.getToken(), "CONT1");

                                    }

                                    // Perform operation
                                    Double result;
                                    switch (p.getToken())
                                    {
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
                                        throw new UnexpectedTokenException(
                                                p.getToken(),
                                                "PLUS or MINUS or DIVIDE or MULTIPLY");
                                    }
                                    }

                                    // Send back result
                                    this.output.writeObject(new Protocol(
                                            Token.CONST2, String
                                                    .valueOf(result)));

                                    // Continue, and loop to process next
                                    // operation.

                                }
                                    break;
                                case ERROR:
                                {
                                    // Error
                                    // Close connection
                                    this.output.close();
                                    this.input.close();
                                    this.socket.close();
                                }
                                    break;
                                case QUIT:
                                {
                                    // Send QUIT
                                    System.out.println("Connection quitting.");
                                    if (!this.socket.isOutputShutdown())
                                    {
                                        this.output.writeObject(new Protocol(
                                                Token.QUIT, "QUIT"));
                                    }
                                    // Close connection
                                    this.output.close();
                                    this.input.close();
                                    this.socket.close();
                                }
                                    break;
                                default:
                                {
                                    // Unexpected operation token.
                                    throw new UnexpectedTokenException(
                                            p.getToken(),
                                            "PLUS or MINUS or DIVIDE or MULTIPLY or QUIT");
                                }
                                }

                            }
                        } else
                        {
                            throw new UnexpectedTokenException(p.getToken(),
                                    "CONNECT");
                        }
                    } catch (UnexpectedTokenException e)
                    {
                        // Send ERROR
                        if (!this.socket.isOutputShutdown())
                        {
                            this.output.writeObject(new Protocol(Token.ERROR, e
                                    .getMessage()));
                        }
                        // Close connection
                        this.output.close();
                        this.input.close();
                        this.socket.close();
                        //
                        e.printStackTrace();
                    } catch (UnexpectedArgumentException e)
                    {
                        // Send ERROR
                        if (!this.socket.isOutputShutdown())
                        {
                            this.output.writeObject(new Protocol(Token.ERROR, e
                                    .getMessage()));
                        }
                        // Close connection
                        this.output.close();
                        this.input.close();
                        this.socket.close();
                        //
                        e.printStackTrace();
                    }

                } catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ClassNotFoundException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } // End run loop

        } catch (IOException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        System.out.println("Closed Server.");
    }

    /**
     * Start the server application.
     * 
     * @param args
     *            Command-Line Arguments.
     */
    public static void main(String[] args)
    {
        Server m = new Server();
        if (args.length == 1)
        {
            // Use args
            m.listen(Integer.parseInt(args[0]));
        } else
        {
            // Default
            m.listen(6666);
        }
    }

}
