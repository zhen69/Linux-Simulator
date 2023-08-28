package src;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * The BashTerminal class acts like the terminal, where it permits users to enter Linux command lines
 * to modify the File Hierarchy Structure.
 *
 * @author Zhen Wei Liao
 */
public class BashTerminal {
    private static DirectoryTree structure;
    private static Scanner input;

    /**
     * Determines which ls command (ls or ls -R) should be run based on the argument <code>arg</code>.
     *
     * @param arg
     *      Argument of the ls command.
     *
     * @throws IllegalArgumentException
     *      when the entered argument is invalid for ls.
     */
    private static void lsCommand(String arg){
        if(arg == null)
            System.out.println(structure.listDirectoryFile());
        else if(arg.equals("-R")){
            System.out.println();
            structure.printStructure();
            System.out.println();
        }
        else
            throw new IllegalArgumentException("Error: Invalid ls command.");
    }

    /**
     * Determines which cd command (cd, cd .., cd /, or cd path) should be run based on the argument <code>arg</code>.
     *
     * @param arg
     *      Argument of the cd command.
     *
     * @throws IllegalArgumentException
     *      when the entered argument is empty/null.
     *
     */
    private static void cdCommand(String arg) throws NotADirectoryException, DFNotFoundException {
        if(arg == null)
            throw new IllegalArgumentException("Error: Invalid cd command.");

        if(arg.equals("/"))
            structure.resetCursor();
        else if(arg.contains("/"))
            structure.changeDirectoryWithPath(arg);
        else if(arg.equals(".."))
            structure.moveToParent();
        else
            structure.changeDirectory(arg);
    }

    /**
     * Runs the mv command by taking <code>paths[0]</code> as the source path and <code>paths[1]</code>
     * as the destination path.
     *
     * @param paths
     *      A String array containing the absolute paths for both source and destination.
     *
     * @throws IllegalArgumentException
     *      when more/less than 2 paths are entered.
     */
    private static void mvCommand(String[] paths) throws DFNotFoundException, NotADirectoryException,
            FullDirectoryException {
        if(paths.length != 2)
            throw new IllegalArgumentException("Error: Invalid mv command.");
        structure.move(paths[0], paths[1]);
    }

    /**
     * Determines which rm command should be run (rm or rm -r)
     * based on <code>args[0]</code> and <code>args[1]</code>.
     *
     * <code>args[2], args[3], args[4]...</code> indicates the directories/files to be removed
     * from the working directory.
     *
     * @param args
     *      A String array indicating which rm commands should be run and which directories/files should
     *      be removed.
     *
     * @throws IllegalArgumentException
     *      when <code>args</code> is empty.
     */
    private static void rmCommand(String[] args) throws DFNotFoundException {
        int len = args.length;
        if(len == 0)
            throw new IllegalArgumentException("Error: Invalid rm command.");
        boolean removeDirectory = false;
        int i = 0;
        if(args[0].equals("-r")) {
            removeDirectory = true;
            i = 1;
        }
        for(; i < len; i++)
            structure.remove(args[i], removeDirectory);
    }

    /**
     * Determines which command is entered and processes the corresponding method/operation.
     *
     * @param cmd
     *      The command part of the user input.
     *
     * @param args
     *      Arguments of the input command.
     */
    private static void commands(String cmd, String args) throws NotADirectoryException, FullDirectoryException,
            DFNotFoundException {
        switch (cmd) {
            case "pwd" -> System.out.println(structure.presentWorkingDirectory());
            case "ls" -> lsCommand(args);
            case "cd" -> cdCommand(args);
            case "mkdir" -> structure.makeDirectory(args);
            case "touch" -> structure.makeFile(args);
            case "mv" -> mvCommand(args.split(" "));
            case "find" -> structure.find(args);
            case "rm" -> rmCommand(args.split(" "));
            default -> System.out.println("Please enter a valid command.");
        }
    }

    /**
     * Based on the input command <code>cmd</code>, determines whether the system should
     * continue to run or terminate.
     *
     * @param cmd
     *      Command input by the user.
     *
     * @return
     *      True if continues to run the bash terminal, otherwise false.
     *
     * @throws IllegalArgumentException
     *      when <code>cmd</code> is null/empty.
     */
    private static boolean runCommands(String cmd) throws NotADirectoryException, FullDirectoryException,
            DFNotFoundException {
        if(cmd == null || cmd.isEmpty())
            throw new IllegalArgumentException("Error: Invalid command.");
        if(cmd.equals("exit"))
            return false;
        int indexToSeparateCmd = cmd.indexOf(" ");
        String args = null;
        if(indexToSeparateCmd != -1){
            args = cmd.substring(indexToSeparateCmd + 1);
            cmd = cmd.substring(0, indexToSeparateCmd);
        }
        commands(cmd, args);
        return true;
    }

    /**
     * Takes input from the user and runs the system.
     *
     * @param user
     *      Username.
     */
    private static void startLinux(String user){
        while(true){
            System.out.print(user + ": ~/" + structure.getCursor().getPath() + "$ ");
            try{
                if(!(runCommands(input.nextLine().trim()))){
                    System.out.println("Bash terminating...");
                    input.close();
                    break;
                }
            } catch(IllegalArgumentException | InputMismatchException | NotADirectoryException |
                    FullDirectoryException | DFNotFoundException e){
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Starts the bash terminal by asking the user to input a username.
     */
    public static void main(String[] args){
        structure = new DirectoryTree();
        System.out.println("Starting Bash Terminal...");
        System.out.print("Please enter a username: ");
        input = new Scanner(System.in);
        String user = input.nextLine().trim() + "@my-doge-ate-my-program";
        startLinux(user);
    }
}
