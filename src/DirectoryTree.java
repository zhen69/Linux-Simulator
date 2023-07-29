package src;

import java.util.Arrays;

/**
 * The DirectoryTree class represents the Linux File Hierarchy Structure. It contains a reference to the root
 * directory, a cursor pointing to the working directory, and methods for modifying/displaying the structure.
 *
 * @author Zhen Wei Liao
 */
public class DirectoryTree {
    private final DirectoryOrFile root = new DirectoryOrFile("root");
    private DirectoryOrFile cursor = root;

    /**
     * Constructor creates a DirectoryTree object with only the root directory presented.
     */
    public DirectoryTree(){
    }

    /**
     * Moves back to the root directory.
     */
    public void resetCursor(){
        cursor = root;
    }

    /**
     * Accessor. Returns a reference to the cursor or working directory.
     *
     * @return
     *      Reference of the cursor.
     */
    DirectoryOrFile getCursor(){
        return cursor;
    }

    /**
     * Checks if <code>df</code> is referencing a valid DirectoryOrFile object.
     *
     * @param df
     *      The DirectoryOrFile object that's being checked.
     *
     * @param checkDirectory
     *      True if checking <code>df</code> is a valid directory, false otherwise.
     *
     * @throws NotADirectoryException
     *      when <code>df</code> reference a file.
     *
     * @throws DFNotFoundException
     *      when <code>df</code> is null.
     */
    private void checkDF(DirectoryOrFile df, boolean checkDirectory)
            throws NotADirectoryException, DFNotFoundException {
        if(df == null)
            throw new DFNotFoundException("Error: Can't find directory.");

        if(checkDirectory && df.isFile())
            throw new NotADirectoryException("Error: Cannot cannot change directory to a file.");
    }

    /**
     * Moves to a directory with the input name. Only directories/files within the working directory
     * would be considered.
     *
     * @param name
     *      Name of the desired directory.
     *
     * @throws IllegalArgumentException
     *      when <code>name</code> is empty or null.
     *
     */
    public void changeDirectory(String name) throws NotADirectoryException, DFNotFoundException {
        if(name == null || name.isEmpty())
            throw new IllegalArgumentException("Error: Name cannot be empty.");
        if(name.equals(cursor.getName()))
            return;
        DirectoryOrFile directory = cursor.find(name);
        checkDF(directory, true);
        cursor = directory;
    }

    /**
     * Returns the desired DirectoryOrFile object.
     *
     * @param pathArr
     *      The path to the wanted DirectoryOrFile object.
     *
     * @param directory
     *      True if the path must leads to a directory, false otherwise.
     *
     * @return
     *      The wanted DirectoryOrFile object.
     */
    private DirectoryOrFile moveByPath(String[] pathArr, boolean directory)
            throws DFNotFoundException, NotADirectoryException {
        DirectoryOrFile df;
        int i;

        if(pathArr[0].equals("root")) {
            i = 1;
            df = root;
        }
        else {
            i = 0;
            df = cursor;
        }

        for(; i < pathArr.length; i++){
            df = df.find(pathArr[i]);
            checkDF(df, directory);
        }

        return df;
    }

    /**
     * Moves to a directory based on the given path.
     *
     * @param path
     *      The path of the desired directory.
     */
    public void changeDirectoryWithPath(String path) throws NotADirectoryException, DFNotFoundException {
        if(path == null || !path.contains("/"))
            throw new IllegalArgumentException("Error: Input path is invalid.");
        if(path.equals(cursor.getPath()))
            return;
        String[] pathArr = path.trim().split("/");
        if(pathArr.length < 1)
            throw new IllegalArgumentException("Error: Input path is invalid.");
        cursor = moveByPath(pathArr, true);
    }

    /**
     * Creates a directory with the input name and stores it in the working directory.
     *
     * @param name
     *      Name of the created directory.
     *
     * @throws IllegalArgumentException
     *      when name contains a forward slash or is empty/null.
     *
     */
    public void makeDirectory(String name) throws NotADirectoryException, FullDirectoryException {
        if(name == null || name.isEmpty() || name.contains("/"))
            throw new IllegalArgumentException("Error: Invalid name \"" + (name == null ? "" : name)  + "\"");
        cursor.addChild(new DirectoryOrFile(name.replaceAll(" ", "_")));
    }

    /**
     * Creates a file with the input name and stores it in the working directory.
     *
     * @param name
     *      Name of the created file.
     *
     * @throws IllegalArgumentException
     *      when name contains a forward slash or is empty/null.
     */
    public void makeFile(String name) throws NotADirectoryException, FullDirectoryException {
        if(name == null || name.isEmpty() || name.contains("/"))
            throw new IllegalArgumentException("Error: Invalid name \"" + (name == null ? "" : name) + "\"");
        cursor.addChild(new DirectoryOrFile(name.replaceAll(" ", "_"), true));
    }

    /**
     * Prints the entire File Hierarchy Structure.
     */
    public void printStructure(){
        root.printStructure(1);
    }

    /**
     * Returns the absolute path of the working directory.
     *
     * @return
     *      A string representation that indicates the path from the root to the working directory.
     */
    public String presentWorkingDirectory(){
        return cursor.getPath();
    }

    /**
     * Views <code>df</code> as the root and traverses through the entire structure to find a
     * directory/file named <code>name</code>.
     *
     * @param df
     *      Root directory.
     *
     * @param name
     *      The name of the wanted directory/file.
     *
     * @return
     *      If found, Directory/file named <code>name</code>, otherwise null.
     */
    public static DirectoryOrFile search(DirectoryOrFile df, String name){
        if(df == null)
            return null;

        if(df.getName().equals(name))
            return df;

        for(DirectoryOrFile node : df.getChildrenDF()){
            DirectoryOrFile result = search(node, name);
            if(result != null)
                return result;
        }

        return null;
    }

    /**
     * Searches for a directory/file named <code>name</code> and prints out the absolute path to it.
     *
     * @param name
     *      Name of the wanted directory/file.
     *
     * @throws DFNotFoundException
     *      when the wanted directory/file cannot be found in the structure.
     */
    public void find(String name) throws DFNotFoundException {
        if(name == null || name.isEmpty())
            throw new IllegalArgumentException("Error: Invalid directory/file name.");

        DirectoryOrFile dfFound = search(root, name);

        if(dfFound == null)
            throw new DFNotFoundException("Error: No such directory/file \"" + name +"\" exist.");

        System.out.println(dfFound.getPath());
    }

    /**
     * Returns a string representation of all the directories/files within the working directory.
     *
     * @return
     *      A space-separated string that lists out the names of the directories/files within the
     *      working directory.
     *
     */
    public String listDirectoryFile(){
        return cursor.printChildrenDF();
    }


    /**
     * Moves to the parent of the working directory.
     *
     * @throws IllegalArgumentException
     *      when the working directory is the root directory
     */
    public void moveToParent(){
        if(cursor == root)
            throw new IllegalArgumentException("Error: Already at root directory.");

        cursor = cursor.getParent();
    }


    /**
     * Removes a directory/file with the specified name from the working directory.
     * @param name
     *      Name of the removed directory/file.
     *
     * @throws DFNotFoundException
     *      when no directory/file with the name <code>name</code> can be found in
     *      the working directory.
     */
    public void remove(String name, boolean directory) throws DFNotFoundException {
        if(cursor.removeChild(name, directory) == null)
            throw new DFNotFoundException("Error: \"" + name + "\" does not exist in the current " +
                    "working directory.");
    }

    /**
     * Moves a directory/file to another directory (destination directory).
     *
     * @param srcPath
     *      Path of the moved directory/file.
     *
     * @param dstPath
     *      Path of the destination directory.
     */
    public void move(String srcPath, String dstPath) throws DFNotFoundException, NotADirectoryException, FullDirectoryException {
        if(srcPath.equals("root") || (dstPath.contains(srcPath) && srcPath.length() < dstPath.length()))
            throw new IllegalArgumentException("Error: Cannot move a parent directory to a child directory.");

        if(!(srcPath.startsWith("root") && dstPath.startsWith("root")))
            throw new IllegalArgumentException("Error: Invalid absolute path.");

        String[] srcPathArr = srcPath.split("/"), dstPathArr = dstPath.split("/");
        if(Arrays.equals(srcPathArr, dstPathArr))
            return;
        DirectoryOrFile srcPtr = moveByPath(srcPathArr, false), dstPtr = moveByPath(dstPathArr, true);
        dstPtr.addChild(srcPtr.getParent().removeChild(srcPtr.getName(), !srcPtr.isFile()));
    }

}
