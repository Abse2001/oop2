

import java.util.*;

public class LibraryManagementSystem {
    private Library library;

    public LibraryManagementSystem() {
        library = new Library();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        String command;
        System.out.println("\n\n\n\n");
        System.out.println("            Welcome to the Library Management System!\n\n");
        System.out.println("Type 'help' for a list of commands.\n");

        while (true) {
            System.out.print("> ");
            command = scanner.nextLine().trim().toLowerCase();

            switch (command.split(" ")[0]) {
                case "help":
                    if (command.equals("help")) {
                        help();
                    } else if (command.startsWith("help add")) {
                        System.out.println("\nAdd Book Usage:");
                        System.out.println("add book <title> <category> <author> <email> <quantity>");
                        System.out.println("Example: add book JavaProgramming Technical JohnDoe john@email.com 5");
                        System.out.println("\nAdd CD Usage:");
                        System.out.println("add cd <title> <company> <duration> <price> <quantity>");
                        System.out.println("Example: add cd AlbumName MusicCo 45 12.99 3");
                    } else {
                        showHelpForCommand(command);
                    }
                    break;

                case "add":
                    addItem(command);
                    break;

                case "remove":
                    removeItem(command);
                    break;

                case "find":
                    findItem(command);
                    break;

                case "borrow":
                    borrowItem(command);
                    break;

                case "return":
                    returnItem(command);
                    break;

                case "list":
                    if (command.equals("list all") || command.equals("list book") || command.equals("list books")) {
                        library.listAllItems();
                    } else {
                        System.out.println("Invalid list command! Use 'list all'.");
                    }
                    break;

                case "exit":
                    System.out.println("Exiting the system. Goodbye!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid command! Type 'help' for available commands or help <command name> for command details.");
                    break;
            }
        }
    }

    private void help() {
        System.out.println("Available commands:");
        System.out.println("add book <title> <category> <author> <email> <quantity>");
        System.out.println("add cd <title> <company> <duration> <price> <quantity>");
        System.out.println("remove <title>");
        System.out.println("find <search term>");
        System.out.println("borrow <title>");
        System.out.println("return <title>");
        System.out.println("list all");
        System.out.println("exit");
    }

    private void showHelpForCommand(String command) {
        String cmd = command.substring(5).trim().toLowerCase();

        switch (cmd) {
            case "add":
                System.out.println("Usage: add <title> <category> <author> <email> <quantity>");
                System.out.println("Adds a new book or increases quantity if it already exists.");
                break;
            case "remove":
                System.out.println("Usage: remove <title> <author>");
                System.out.println("Removes a book from the library.");
                break;
            case "find":
                System.out.println("Usage: find <title>");
                System.out.println("Searches for a book by title.");
                break;
            case "borrow":
                System.out.println("Usage: borrow <title> <author>");
                System.out.println("Borrows a book and decreases the available quantity by 1.");
                break;
            case "return":
                System.out.println("Usage: return <title> <author>");
                System.out.println("Returns a borrowed book and increases the available quantity by 1.");
                break;
            case "list all":
                System.out.println("Usage: list all");
                System.out.println("Lists all books with their details.");
                break;
            case "exit":
                System.out.println("Usage: exit");
                System.out.println("Exits the system.");
                break;
            default:
                System.out.println("Unknown command: " + cmd);
                break;
        }
    }

    private void addItem(String command) {
        String[] parts = command.split(" ");
        
        if (parts.length < 2) {
            System.out.println("Invalid command format! Type 'help add' for usage information.");
            return;
        }

        String type = parts[1].toLowerCase();
        
        if (type.equals("book")) {
            if (parts.length != 7) {
                System.out.println("Invalid command format! Usage: 'add book <title> <category> <author> <email> <quantity>'");
                return;
            }
            addBook(parts);
        } else if (type.equals("cd")) {
            if (parts.length != 7) {
                System.out.println("Invalid command format! Usage: 'add cd <title> <company> <duration> <price> <quantity>'");
                return;
            }
            addCD(parts);
        } else {
            System.out.println("Invalid item type! Use 'book' or 'cd'");
        }
    }

    private void addBook(String[] parts) {
        try {
            String title = parts[2];
            String category = parts[3];
            String author = parts[4];
            String email = parts[5];
            int quantity = Integer.parseInt(parts[6]);
            
            if (quantity <= 0) {
                System.out.println("Quantity must be greater than 0!");
                return;
            }
            
            Book book = new Book(title, category, author, email, quantity);
            library.addItem(book);
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity! Please enter a valid integer for the quantity.");
        }
    }

    private void addCD(String[] parts) {
        try {
            String title = parts[2];
            String company = parts[3];
            int duration = Integer.parseInt(parts[4]);
            double price = Double.parseDouble(parts[5]);
            int quantity = Integer.parseInt(parts[6]);
            
            if (quantity <= 0) {
                System.out.println("Quantity must be greater than 0!");
                return;
            }
            if (duration <= 0) {
                System.out.println("Duration must be greater than 0!");
                return;
            }
            if (price <= 0) {
                System.out.println("Price must be greater than 0!");
                return;
            }
            
            CD cd = new CD(title, company, duration, price, quantity);
            library.addItem(cd);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format! Please check duration, price and quantity values.");
        }
    }
    

    private void removeItem(String command) {
    String[] parts = command.split(" ");
    if (parts.length != 3) {
        System.out.println("Invalid command! Usage: remove <title> <author>");
        return;
    }

    String title = parts[1];
    String author = parts[2];

    Item item = library.findItemByTitle(title, author);
    
    if (item == null) {
        System.out.println("No item found with the title: " + title);
        return;
    }
    
    if (item instanceof Book && ((Book)item).getAuthor().equalsIgnoreCase(author)) {
        library.removeItem(title, author, new HashMap<String, String>());
        System.out.println("Item removed: " + item.displayInfo());
    } else {
        System.out.println("No book found with the title '" + title + "' and author '" + author + "'.");
    }
}

    private void findItem(String command) {
        String[] parts = command.split(" ");
        if (parts.length != 3) {
            System.out.println("Invalid command! Usage: find <title> <author>");
            return;
        }
        String title = parts[1];
        String author = parts[2];
        library.findItem(title, author, new HashMap<String, String>());
    }

    private void borrowItem(String command) {
        String[] parts = command.split(" ");
        if (parts.length < 3) {
            System.out.println("Invalid command! Usage: borrow <title> <author>");
            return;
        }
    
        String title = parts[1];
        String author = parts[2];
    
        Item item = library.findItemByTitle(title, author);
        
        if (item == null) {
            System.out.println("No item found with the title: " + title);
            return;
        }
        
        if (item instanceof Book && ((Book)item).getAuthor().equalsIgnoreCase(author)) {
            library.borrowItem(title, author, new HashMap<String, String>());
        } else {
            System.out.println("No book found with the title '" + title + "' and author '" + author + "'.");
        }
    
    }

    private void returnItem(String command) {
        String[] parts = command.split(" ");
        if (parts.length < 3) {
            System.out.println("Invalid command! Usage: return <title> <author>");
            return;
        }
    
        String title = parts[1];
        String author = parts[2];
    
        Item item = library.findItemByTitle(title, author);
        
        if (item == null) {
            System.out.println("No item found with the title: " + title);
            return;
        }
        
        if (item instanceof Book && ((Book)item).getAuthor().equalsIgnoreCase(author)) {
            library.returnItem(title, author, new HashMap<String, String>());
        } else {
            System.out.println("No book found with the title '" + title + "' and author '" + author + "'.");
        }
    
    }

    public static void main(String[] args) {
        LibraryManagementSystem system = new LibraryManagementSystem();
        system.run();
    }
}
