package src;

/**
 * The DirectoryOrFile class represents a directory/file object. It contains information such as the name
 * of the instance, the path to it, the indicator of whether it's a directory or not,
 * and all the directories/files that are contained in the instance.
 *
 * @author Zhen Wei Liao
 */
public class DirectoryOrFile {
    private String name;
    private boolean isFile = false;
    private DirectoryOrFile[] links = new DirectoryOrFile[10];
    private String path = "root";
    private int numOfChildren = 0;


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
    public DirectoryOrFile[] getLinks() {
        return links;
    }

    /**
     * Modifier. Modifies the contents/links of the current instance.
     *
     * @param links
     *      Array containing other DirectoryOrFile objects that connects to the current instance.
     */
    public void setLinks(DirectoryOrFile[] links) {
        this.links = links;
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
     * Indicates whether the current instance has reached a maximum of 10 links.
     *
     * @return
     *      True if the instance has exactly 10 links, false otherwise.
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
        for(DirectoryOrFile df : links){
            if(df != null && df.name.equals(name))
                return df;
        }
        return null;
    }

    /**
     * Returns a string representation of all the directories/files that are inside the current instance.
     *
     * @return
     *      A space-separated string containing all the names of the linked directories/files.
     *
     * @throws IllegalArgumentException
     *      when method is called on a file.
     */
    public String printLinks(){
        if(isFile)
            throw new IllegalArgumentException("Error: File cannot contain directories/files.");

        StringBuilder str = new StringBuilder();

        for(DirectoryOrFile df : links)
            if(df != null)
                str.append(df.name).append(" ");

        if(str.length() > 0)
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

            for(DirectoryOrFile df : links){
                if(df != null)
                    df.printStructure(++height);
            }
        }
    }

    /**
     * Adds a directory/file to the current instance.
     *
     * @param newLink
     *      A DirectoryOrFile object connecting to the current instance.
     *
     * @throws FullDirectoryException
     *      when the current instance has reached a maximum of 10 links.
     *
     * @throws NotADirectoryException
     *      when the current instance is a file.
     */
    public void addLink(DirectoryOrFile newLink) throws FullDirectoryException, NotADirectoryException{
        if(isFile)
            throw new NotADirectoryException("Error: Cannot add directory/file to a file.");

        if(isFull())
            throw new FullDirectoryException("Error: Current directory is full.");

        if(find(newLink.name) != null)
            throw new IllegalArgumentException("Error: Directory/File \"" + newLink.name +
                    "\" already existed in the current directory.");

        for(int i = 0; i < links.length; i++){
            if(links[i] == null){
                newLink.setPath(path + "/" + newLink.name);
                links[i] = newLink;
                numOfChildren++;
                break;
            }
        }
    }
}
