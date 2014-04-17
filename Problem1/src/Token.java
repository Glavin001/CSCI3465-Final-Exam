/**
 * @author Glavin Wiechert
 * 
 */
public enum Token
{

    /**
     * Arithmetic Operators
     */
    PLUS, MINUS, MULTIPLY, DIVIDE,

    CONST1, // Operand
    CONST2, // Result

    /**
     * States
     */
    CONNECT, READY, ERROR, QUIT

}
