package src;

/**
 * The NotADirectoryException class inherit all behaviors and properties of the Exception class. This exception
 * is thrown when an object is not a directory.
 *
 * @author Zhen Wei Liao
 */
public class NotADirectoryException extends Exception {
    /**
     * Constructor calls the constructor of the Exception class.
     */
    public NotADirectoryException(){
        super();
    }

    /**
     * Constructor calls the constructor of the Exception class with a specified error message.
     *
     * @param error
     *      Error message to display when this exception is thrown.
     */
    public NotADirectoryException(String error){
        super(error);
    }
}
