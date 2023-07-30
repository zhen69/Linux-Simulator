package src;

import java.util.Arrays;
import java.util.InputMismatchException;

/**
 * The DirectoryOrFile class represents a directory/file object. It contains information such as the name
 * of the instance, the path to it, an indicator of whether it's a directory or not, a pointer to its parent
 * directory, and all the directories/files that are contained in the instance.
 *
 * @author Zhen Wei Liao
 */
public class DirectoryOrFile {
    private String name;
    private boolean isFile = false;
    private DirectoryOrFile[] childrenDF = new DirectoryOrFile[10];
    private String path = "root";
    private int numOfChildren = 0;
    private DirectoryOrFile parent = null;


    /**
     * Constructor creates a DirectoryOrFile object that takes the default value of the attributes.
     */
    public DirectoryOrFile(){
    }

    /**
     * Constructor creates a directory with a specified name.
     *
     * @param name
     *      The name of the directory.
     */
    public DirectoryOrFile(String name){
        this.name = name;
    }

    /**
     * Constructor creates a directory or file with a specified name.
     *
     * @param name
     *      The name of the directory/file.
     *
     * @param isFile
     *      Indicates whether the current instance is a directory or file.
     */
    public DirectoryOrFile(String name, boolean isFile){
        this.name = name;
        this.isFile = isFile;
    }


    /**
     * Accessor. Returns the name of the DirectoryOrFile instance.
     *
     * @return
     *      The name of the directory/file.
     */
    public String getName() {
        return name;
    }

    /**
     * Modifier. Modifies the name of the DirectoryOrFile instance.
     *
     * @param name
     *      The name of the directory/file.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Accessor. Indicates the instance is a directory or file.
     *
     * @return
     *      True if the instance is a file.
     *      False if the instance is a directory.
     */
    public boolean isFile() {
        return isFile;
    }

    /**
     * Modifier. Modifies the type of the instance.
     *
     * @param isFile
     *      True if the instance is a file.
     *      False if the instance is a directory.
     */
    public void setFile(boolean isFile) {
        this.isFile = isFile;
    }

    /**
     * Accessor. Returns an array of the directories/files within the current instance.
     *
     * @return
     *      Array containing other DirectoryOrFile objects that connects to the current instance.
     */
    public DirectoryOrFile[] getChildrenDF() {
        return childrenDF;
    }

    /**
     * Modifier. Modifies the contents/children of the current instance.
     *
     * @param childrenDF
     *      Array containing other DirectoryOrFile objects that connects to the current instance.
     */
    public void setChildrenDF(DirectoryOrFile[] childrenDF) {
        this.childrenDF = childrenDF;
    }

    /**
     * Accessor. Returns the path to the current instance.
     *
     * @return
     *      A string indicating the path to the current instance.
     */
    public String getPath() {
        return path;
    }

    /**
     * Modifier. Modifies the path to the current instance.
     *
     * @param path
     *      A string indicating the path to the current instance.
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Accessor. Returns the parent directory of the current instance.
     *
     * @return
     *      Parent Directory of the current instance.
     */
    public DirectoryOrFile getParent(){ return parent;}

    /**
     * Indicates whether the current instance has reached a maximum of 10 childrenDF.
     *
     * @return
     *      True if the instance has exactly 10 childrenDF, false otherwise.
     */
    public boolean isFull(){
        return (numOfChildren == 10);
    }

    /**
     * Returns the DirectoryOrFile object with the input name.
     *
     * @return
     *      DirectoryOrFile object with the input name if present, else null.
     *
     */
    public DirectoryOrFile find(String name){
        for(DirectoryOrFile df : childrenDF){
            if(df != null && df.name.equals(name))
                return df;
        }
        return null;
    }

    /**
     * Returns a string representation of all the directories/files that are inside the current instance.
     *
     * @return
     *      A space-separated string containing all the names of the children directories/files.
     *
     * @throws IllegalArgumentException
     *      when method is called on a file.
     */
    public String printChildrenDF(){
        if(isFile)
            throw new IllegalArgumentException("Error: File cannot contain directories/files.");

        StringBuilder str = new StringBuilder();

        for(DirectoryOrFile df : childrenDF)
            if(df != null)
                str.append(df.name).append(" ");

        if(!str.isEmpty())
            return str.toString();
        else
            return "Current directory has no directories/files.";
    }

    /**
     * Views the current instance as the root and print out all the directories/files under it.
     *
     * @param height
     *      The current level of the directory/file.
     *
     * @throws IllegalArgumentException
     *      when height is zero or negative.
     *
     */
    public void printStructure(int height){
        if(height < 1)
            throw new IllegalArgumentException("Error: Height must be positive.");

        String indent = "    ".repeat(height - 1);
        if(isFile())
            System.out.println(indent + "- " + name);
        else{
            System.out.println(indent + "|- " + name);
            height++;
            for(DirectoryOrFile df : childrenDF){
                if(df != null)
                    df.printStructure(height);
            }
        }
    }

    /**
     * Adds a directory/file to the current instance.
     *
     * @param newChild
     *      A DirectoryOrFile object connecting to the current instance.
     *
     * @throws FullDirectoryException
     *      when the current instance has reached a maximum of 10 childrenDF.
     *
     * @throws NotADirectoryException
     *      when the current instance is a file.
     */
    public void addChild(DirectoryOrFile newChild) throws FullDirectoryException, NotADirectoryException{
        if(isFile)
            throw new NotADirectoryException("Error: Cannot add directory/file to a file.");

        if(isFull())
            throw new FullDirectoryException("Error: Current directory is full.");

        if(find(newChild.name) != null)
            throw new IllegalArgumentException("Error: Directory/File \"" + newChild.name +
                    "\" already existed in the current directory.");

        for(int i = 0; i < childrenDF.length; i++){
            if(childrenDF[i] == null){
                newChild.setPath(path + "/" + newChild.name);
                newChild.parent = this;
                childrenDF[i] = newChild;
                numOfChildren++;
                break;
            }
        }
    }

    /**
     * Helper method for removeLink.
     * Shifts all items with an index greater than <code>position</code> to the left by one unit.
     *
     * @param position
     *      Index of the removed DirectoryOrFile object.
     */
    private void shift(int position){
        int len = childrenDF.length;
        for(int i = position; i < len - 1; i++)
            childrenDF[i] = childrenDF[i+1];
        childrenDF[len - 1] = null;
    }

    /**
     * Removes a directory/file with the input name from the current instance.
     *
     * @param df
     *      A string indicating the name of the directory/file to be removed.
     *
     * @param directory
     *      True if <code>df</code> is a directory, false if <code>df</code> is a file.
     *
     * @return
     *      If present in the current instance, returns the removed DirectoryOrFile object,
     *      otherwise null.
     *
     * @throws InputMismatchException
     *      when <code>directory</code> indicates <code>df</code> is a directory but the actual
     *      object is a file.
     *      Or, when <code>directory</code> indicates <code>df</code> is a file but the actual
     *      object is a directory.
     */
    public DirectoryOrFile removeChild(String df, boolean directory){
        if(df == null || df.isEmpty())
            return null;

        for(int i = 0; i < childrenDF.length; i++){
            if(childrenDF[i] != null && childrenDF[i].name.equals(df)) {
                DirectoryOrFile removedDF = childrenDF[i];
                if((!removedDF.isFile && !directory) || (directory && removedDF.isFile))
                    throw new InputMismatchException("Error: Can't remove \"" + df + "\": Is "
                            + (removedDF.isFile ? "File" : "Directory"));
                shift(i);
                return removedDF;
            }
        }
        return null;
    }

    /**
     * Checks if the equality of two DirectoryOrFile objects based on their names and types.
     *
     * @param obj
     *      Object being compared to current instance.
     *
     * @return
     *      True if two DirectoryOrFile objects has the same name and type, otherwise false.
     */
    @Override
    public boolean equals(Object obj){
        if(obj == this) return true;
        if(!(obj instanceof DirectoryOrFile)) return false;

        DirectoryOrFile objAsDF = (DirectoryOrFile) obj;

        return (isFile == objAsDF.isFile && name.equals(objAsDF.name)
                && Arrays.equals(childrenDF, objAsDF.childrenDF));
    }

    /**
     * Returns a string representation of the DirectoryOrFile object.
     *
     * @return
     *      A string representation of the DirectoryOrFile object in the format of "type: name"
     */
    @Override
    public String toString(){
        return (isFile ? "File" : "Directory") + ": " + name;
    }
}
