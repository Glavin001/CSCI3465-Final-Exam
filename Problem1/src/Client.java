import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * @author Glavin Wiechert
 * 
 */
public class Client
{

    /**
     * Socket connection.
     */
    Socket socket;
    /**
     * Input stream.
     */
    ObjectInputStream input;
    /**
     * Output stream.
     */
    ObjectOutputStream output;

    /**
     * Keyboard input.
     */
    Scanner keyboard;

    /**
     * Flag if client is currently running.
     */
    boolean isRunning;

    /**
     * Constructor
     * 
     * @param hostname
     *            The hostname.
     * @param port
     *            The post number.
     */
    public Client(String hostname, int port)
    {
        // Connect to server
        try
        {
            this.socket = new Socket(hostname, port);
            this.output = new ObjectOutputStream(this.socket.getOutputStream());
            this.input = new ObjectInputStream(this.socket.getInputStream());
            this.keyboard = new Scanner(System.in);
        } catch (UnknownHostException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Start the Command-Line Interface
     */
    public void start()
    {
        isRunning = true;
        try
        {
            try
            {
                // Send CONNECT
                this.output.writeObject(new Protocol(Token.CONNECT, null));

                // Loop while running
                while (isRunning && !this.socket.isClosed())
                {

                    System.out.println("Ready for command.");
                    // Wait for READY message
                    Protocol p = (Protocol) input.readObject();
                    if (p.getToken().equals(Token.READY))
                    {

                        System.out.println("Server is READY.");

                        // Prompt for input
                        System.out
                                .print("Enter command (PLUS, MINUS, MULTIPLY, DIVIDE, QUIT): ");
                        String op = keyboard.nextLine();

                        // Perform respective operation
                        Token token = null;
                        switch (op)
                        {
                        // Arithmetic Operations
                        case "PLUS":
                        case "plus":
                        case "add":
                        case "+":
                        {
                            token = Token.PLUS;
                        }
                            break;
                        case "MINUS":
                        case "subtract":
                        case "minus":
                        case "-":
                        {
                            token = Token.MINUS;
                        }
                            break;
                        case "MULTIPLY":
                        case "times":
                        case "multiply":
                        case "*":
                        {
                            token = Token.MULTIPLY;
                        }
                            break;
                        case "DIVIDE":
                        case "divide":
                        case "/":
                        {
                            token = Token.DIVIDE;
                        }
                            break;
                        case "QUIT":
                        case "quit":
                        case "q":
                        {
                            // Send QUIT
                            System.out.println("Quitting...");
                            if (!this.socket.isOutputShutdown())
                            {
                                this.output.writeObject(new Protocol(
                                        Token.QUIT, "QUIT"));
                            }
                            // Wait to receive QUIT
                            p = (Protocol) input.readObject();
                            // Close connection
                            this.isRunning = false;
                            this.output.close();
                            this.input.close();
                            this.socket.close();
                        }
                            break;
                        default:
                        {
                            throw new Exception("Unexpected Operator '" + op
                                    + "'.");
                        }
                        }

                        // Confirm still running
                        // May have QUIT
                        if (isRunning)
                        {

                            // Send Token (Operation)
                            this.output.writeObject(new Protocol(token, null));

                            // Read in two CONSTs
                            System.out.print("Enter first CONST1: ");
                            String c1 = keyboard.nextLine();
                            System.out.print("Enter second CONST1: ");
                            String c2 = keyboard.nextLine();

                            // Send CONSTs as CONST1
                            this.output.writeObject(new Protocol(Token.CONST1,
                                    c1));
                            this.output.writeObject(new Protocol(Token.CONST1,
                                    c2));

                            // Receive result CONST2
                            Protocol result = (Protocol) this.input
                                    .readObject();

                            // Print result
                            System.out.println("Result: "
                                    + result.getArgument());

                            // Continue and start over

                        }

                    } else
                    {
                        throw new UnexpectedTokenException(p.getToken(),
                                "READY");
                    }

                } // end run loop

            } catch (UnexpectedTokenException e)
            {
                error(e);
            } catch (ClassNotFoundException e1)
            {
                error(e1);
            } catch (Exception e)
            {
                error(e);
            }
        } catch (IOException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        System.out.println("Closed client connection.");
    }

    /**
     * Handles an ERROR (token).
     * 
     * @param e
     *            The exception.
     * @throws IOException
     *             if an I/O error occurs
     */
    private void error(Exception e) throws IOException
    {
        // Send ERROR
        if (!this.socket.isOutputShutdown())
        {
            this.output.writeObject(new Protocol(Token.ERROR, e.getMessage()));
        }
        // Close connection
        this.output.close();
        this.input.close();
        this.socket.close();
        //
        e.printStackTrace();
    }

    /**
     * Start the client.
     * 
     * @param args
     *            The CLI arguments.
     */
    public static void main(String[] args)
    {
        Client client;
        if (args.length == 2)
        {
            String hostname = args[0];
            int port = Integer.parseInt(args[1]);

            // Use arguments
            client = new Client(hostname, port);
        } else
        {
            // Use Defaults
            client = new Client("localhost", 6666);
        }
        client.start();
    }

}
