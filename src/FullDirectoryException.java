package src;

/**
 * The FullDirectoryException class inherit all behaviors and properties of the Exception class. This exception
 * is thrown when a Directory has reached the maximum number of children directories/files.
 *
 * @author Zhen Wei Liao
 */
public class FullDirectoryException extends Exception {
    /**
     * Constructor calls the constructor of the Exception class.
     */
    public FullDirectoryException(){
        super();
    }

    /**
     * Constructor calls the constructor of the Exception class with a specified error message.
     *
     * @param error
     *      Error message to display when this exception is thrown.
     */
    public FullDirectoryException(String error){
        super(error);
    }

}
