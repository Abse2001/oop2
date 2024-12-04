import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Library library = new Library();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            String command = scanner.nextLine().trim().toLowerCase();

            if (command.isEmpty()) {
                System.out.println("Invalid command. Type 'help' for a list of available commands.");
                continue;
            }

            if (command.equals("exit")) {
                System.out.println("Exiting the system.");
                break;
            }

            executeCommand(command, library);
        }
        scanner.close();
    }

    private static void executeCommand(String command, Library library) {
        String[] parts = command.split(" ", 2);
        String action = parts[0];

        try {
            switch (action) {
                case "add":
                    if (parts.length < 2) {
                        System.out.println("Usage: add [title];[author];[email];[quantity]");
                        return;
                    }
                    String[] addParts = parts[1].split(";");
                    library.addItem(addParts[0].trim(), addParts[1].trim(), addParts[2].trim(), Integer.parseInt(addParts[3].trim()));
                    break;
                case "remove":
                    if (parts.length < 2) {
                        System.out.println("Usage: remove [title]");
                        return;
                    }
                    library.removeItem(parts[1].trim());
                    break;
                case "find":
                    if (parts.length < 2) {
                        System.out.println("Usage: find [title]");
                        return;
                    }
                    library.findItem(parts[1].trim());
                    break;
                case "borrow":
                    if (parts.length < 2) {
                        System.out.println("Usage: borrow [title]");
                        return;
                    }
                    library.borrowItem(parts[1].trim());
                    break;
                case "return":
                    if (parts.length < 2) {
                        System.out.println("Usage: return [title]");
                        return;
                    }
                    library.returnItem(parts[1].trim());
                    break;
                case "list":
                    if (parts.length < 2) {
                        System.out.println("Usage: list [all|book]");
                        return;
                    }
                    if (parts[1].equalsIgnoreCase("all")) {
                        library.listAllItems();
                    } else if (parts[1].equalsIgnoreCase("book")) {
                        library.listBooks();
                    } else {
                        System.out.println("Invalid list option. Use 'list all' or 'list book'.");
                    }
                    break;
                case "help":
                    if (parts.length > 1) {
                        displayCommandHelp(parts[1]);
                    } else {
                        displayHelp();
                    }
                    break;
                default:
                    System.out.println("Invalid command. Type 'help' for a list of available commands.");
            }
        } catch (Exception e) {
            System.out.println("Error processing command. Ensure proper syntax. Type 'help' for details.");
        }
    }

    private static void displayHelp() {
        System.out.println("Available commands:");
        System.out.println("add [title];[author];[email];[quantity] - Adds a new book or updates quantity.");
        System.out.println("remove [title] - Removes a book by title.");
        System.out.println("find [title] - Finds a book by title.");
        System.out.println("borrow [title] - Borrows a book (decreases quantity).");
        System.out.println("return [title] - Returns a borrowed book (increases quantity).");
        System.out.println("list all - Lists all items.");
        System.out.println("list book - Lists all books.");
        System.out.println("help [command] - Provides details about a specific command.");
        System.out.println("exit - Exits the system.");
    }

    private static void displayCommandHelp(String command) {
        switch (command.toLowerCase()) {
            case "add":
                System.out.println("add [title];[author];[email];[quantity]: Adds a new book or updates the quantity if it exists.");
                break;
            case "remove":
                System.out.println("remove [title]: Removes a book by its title.");
                break;
            case "find":
                System.out.println("find [title]: Finds a book by title and displays its details.");
                break;
            case "borrow":
                System.out.println("borrow [title]: Borrows a book if available.");
                break;
            case "return":
                System.out.println("return [title]: Returns a borrowed book and increases the quantity.");
                break;
            case "list":
                System.out.println("list all: Lists all items.\nlist book: Lists all books with their details.");
                break;
            case "exit":
                System.out.println("exit: Exits the system.");
                break;
            default:
                System.out.println("No help available for the given command. Type 'help' for a list of commands.");
        }
    }
}
