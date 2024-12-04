import java.util.*;

public class Library {
    private List<Book> items = new ArrayList<>();

    // Adds a new item or updates the quantity if the item already exists.
    public void addItem(String title, String author, String email, int quantity) {
        if (title.isEmpty() || author.isEmpty() || email.isEmpty() || quantity <= 0) {
            System.out.println("Invalid input. Ensure all fields are provided correctly.");
            return;
        }

        for (Book item : items) {
            if (item.getTitle().equalsIgnoreCase(title)) {
                item.increaseQuantity(quantity);
                System.out.println("Item already exists. Quantity increased.");
                return;
            }
        }
        items.add(new Book(title, author, email, quantity));
        System.out.println("New item added.");
    }

    // Removes an item by title.
    public void removeItem(String title) {
        if (title.isEmpty()) {
            System.out.println("Title cannot be empty. Provide a valid title.");
            return;
        }

        Iterator<Book> iterator = items.iterator();
        while (iterator.hasNext()) {
            Book item = iterator.next();
            if (item.getTitle().equalsIgnoreCase(title)) {
                iterator.remove();
                System.out.println("Done: Item removed successfully.");
                return;
            }
        }
        System.out.println("Not found: Item not found in the list.");
    }

    // Finds and displays an item by title.
    public void findItem(String title) {
        if (title.isEmpty()) {
            System.out.println("Title cannot be empty. Provide a valid title.");
            return;
        }

        for (Book item : items) {
            if (item.getTitle().equalsIgnoreCase(title)) {
                System.out.println(item + "   " + item.getQuantity());
                return;
            }
        }
        System.out.println("Not found.");
    }

    // Borrows an item by title.
    public void borrowItem(String title) {
        if (title.isEmpty()) {
            System.out.println("Title cannot be empty. Provide a valid title.");
            return;
        }

        for (Book item : items) {
            if (item.getTitle().equalsIgnoreCase(title)) {
                if (item.getQuantity() > 0) {
                    item.decreaseQuantity();
                    System.out.println("Done: Item borrowed successfully.");
                } else {
                    System.out.println("No available item!");
                }
                return;
            }
        }
        System.out.println("Not found: Item not found in the list.");
    }

    // Returns an item by title.
    public void returnItem(String title) {
        if (title.isEmpty()) {
            System.out.println("Title cannot be empty. Provide a valid title.");
            return;
        }

        for (Book item : items) {
            if (item.getTitle().equalsIgnoreCase(title)) {
                item.increaseQuantity(1);
                System.out.println("Done: Item returned successfully.");
                return;
            }
        }
        System.out.println("Not found: Item not found in the list.");
    }

    // Lists all items in the library.
    public void listAllItems() {
        if (items.isEmpty()) {
            System.out.println("The library is empty. No items to display.");
            return;
        }

        for (Book item : items) {
            System.out.println(item + "   " + item.getQuantity());
        }
    }

    // Lists only books in the library.
    public void listBooks() {
        boolean found = false;
        for (Book item : items) {
            System.out.println(item.getTitle() + "   " + item.getAuthor() + "   " + item.getEmail() + "   " + item.getQuantity());
            found = true;
        }
        if (!found) {
            System.out.println("No books found.");
        }
    }
}
