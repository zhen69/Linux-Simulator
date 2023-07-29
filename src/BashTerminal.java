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

    private static void mvCommand(String[] paths) throws DFNotFoundException, NotADirectoryException,
            FullDirectoryException {
        if(paths.length != 2)
            throw new IllegalArgumentException("Error: Invalid mv command.");
        structure.move(paths[0], paths[1]);
    }

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

    private static void startTerminal(String user){
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

    public static void main(String[] args){
        structure = new DirectoryTree();
        System.out.println("Starting Bash Terminal...");
        System.out.print("Please enter a username: ");
        input = new Scanner(System.in);
        String user = input.nextLine().trim() + "@my-doge-ate-my-program";
        startTerminal(user);
    }
}
