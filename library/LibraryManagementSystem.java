import java.util.List;
import java.util.Scanner;

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
                    if (command.equals("list all")) {
                        library.listAllItems();
                    } else {
                        System.out.println("Invalid list command! Use 'list all' or 'list book'.");
                    }
                    break;

                case "exit":
                    System.out.println("Exiting the system. Goodbye!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid command! Type 'help' for available commands.");
                    break;
            }
        }
    }

    private void help() {
        System.out.println("Available commands:");
        System.out.println("add <title> <category> <author> <email> <quantity>");
        System.out.println("remove <title>");
        System.out.println("find <title>");
        System.out.println("borrow <title>");
        System.out.println("return <title>");
        System.out.println("list all");
        System.out.println("list book");
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
        
        if (parts.length == 6) {
            String title = parts[1];
            String category = parts[2];
            String author = parts[3];
            String email = parts[4];
            String quantityStr = parts[5];
            
            if (!quantityStr.matches("\\d+")) {
                System.out.println("Invalid quantity! Please enter a valid integer for the quantity.");
                return;
            }
            
            int quantity = Integer.parseInt(quantityStr);
    
            Book book = new Book(title, category, author, email, quantity);
            library.addItem(book);
            System.out.println("Book : " + book.displayBookInfo() + " added successfully.");
        } else {
            System.out.println("Invalid command format! Usage: 'add <title> <category> <author> <email> <quantity>'");
        }
    }
    

    private void removeItem(String command) {
    String[] parts = command.split(" ");
    if (parts.length < 3) {
        System.out.println("Invalid command! Usage: remove <title> <author>");
        return;
    }

    String title = parts[1];
    String author = parts[2];

    List<Book> matchingBooks = library.findItemsByTitle(title);

    if (matchingBooks.isEmpty()) {
        System.out.println("No books found with the title: " + title + "and author: " + author);

        return;
    }

    boolean bookRemoved = false;
    for (Book book : matchingBooks) {
        if (book.getTitle().equalsIgnoreCase(title) && book.getAuthor().equalsIgnoreCase(author)) {
            library.removeItem(book);
            System.out.println("Book removed: " + book.displayBookInfo());
            bookRemoved = true;
            break;
        }
    }

    if (!bookRemoved) {
        System.out.println("No book found with the title '" + title + "' and author '" + author + "'.");
    }
}

    private void findItem(String command) {
        String title = command.substring(5).trim();
        library.findItem(title);
    }

    private void borrowItem(String command) {
        String[] parts = command.split(" ");
        if (parts.length < 3) {
            System.out.println("Invalid command! Usage: borrow <title> <author>");
            return;
        }
    
        String title = parts[1];
        String author = parts[2];
    
        List<Book> matchingBooks = library.findItemsByTitle(title);
    
        if (matchingBooks.isEmpty()) {
            System.out.println("No books found with the title: " + title + "and author: " + author);
            return;
        }
    
        for (Book book : matchingBooks) {
            if (book.getTitle().equalsIgnoreCase(title) && book.getAuthor().equalsIgnoreCase(author)) {
                library.borrowItem(book);
                break;
            }
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
    
        List<Book> matchingBooks = library.findItemsByTitle(title);
    
        if (matchingBooks.isEmpty()) {
            System.out.println("No books found with the title: " + title + "and author: " + author);
            return;
        }

        for (Book book : matchingBooks) {
            if (book.getTitle().equalsIgnoreCase(title) && book.getAuthor().equalsIgnoreCase(author)) {
                library.returnItem(book);
                break;
            }
        }
    
    }

    public static void main(String[] args) {
        LibraryManagementSystem system = new LibraryManagementSystem();
        system.run();
    }
}
