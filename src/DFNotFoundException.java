package src;

/**
 * The DFNotFoundException class inherit all behaviors and properties of the Exception class.
 * This exception is thrown when a directory or file is not found in the current File Hierarchy Structure.
 *
 * @author Zhen Wei Liao
 */
public class DFNotFoundException extends Exception{
    /**
     * Constructor calls the constructor of the Exception class.
     */
    public DFNotFoundException(){
        super();
    }

    /**
     * Constructor calls the constructor of the Exception class with a specified error message.
     *
     * @param error
     *      Error message to display when this exception is thrown.
     */
    public DFNotFoundException(String error){
        super(error);
    }
}
