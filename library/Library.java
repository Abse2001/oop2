import java.util.ArrayList;
import java.util.List;

public class Library {
    private List<Book> Books;

    public Library() {
        Books = new ArrayList<>();
    }
    public void addItem(Book book) {
        for (Book b : Books) {
            if (b.getTitle().equalsIgnoreCase(book.getTitle()) && b.getAuthor().equalsIgnoreCase(book.getAuthor()) && b.getEmail().equalsIgnoreCase(book.getEmail()) && b.getCategory().equalsIgnoreCase(book.getCategory())) {
                b.setQuantity(b.getQuantity() + book.getQuantity());
                b.setMaxQuantity( b.getMaxQuantity() + book.getQuantity());
                return;
            }
        }
        book.setMaxQuantity(book.getQuantity());
        Books.add(book);
    }

    public void removeItem(String title,String author) {
        for (Book b : Books) {
            if (b.getTitle().equalsIgnoreCase(title) && b.getAuthor().equalsIgnoreCase(author)) {
                Books.remove(b);
                System.out.println("Done");
                return;
            }
        }
        System.out.println("Not found");
    }

    public Book findItem(String title) {
        for (Book b : Books) {
            if (b.getTitle().equalsIgnoreCase(title)) {
                return b;
            }
        }
        System.out.println("Not found");
        return null;
    }

    public void borrowItem(Book book) {
        if (book != null) {
            if (book.getQuantity() > 0) {
                book.setQuantity(book.getQuantity() - 1);
                System.out.println("Book borrowed: " + book.displayBookInfo());

            } else {
                System.out.println("No available item");
            }
        }
    }

    public void returnItem(Book book) {
        if (book.getQuantity() < book.getMaxQuantity()) {
            book.setQuantity(book.getQuantity() + 1);
            System.out.println("Done");
        }else{
            System.out.println("All books are in stock");
            System.out.println("Use add command to add more books");
        }
    }

    public void listAllItems() {
        for (Book book : Books) {
           System.out.println(book.displayBookInfo());
        }
    }

    public void listBooks() {
        for (Book book : Books) {
            if (book.getQuantity() > 0) {
                book.displayBookInfo();
            }
        }
    }
    public List<Book> findItemsByTitle(String title) {
        List<Book> matchingBooks = new ArrayList<>();
        for (Book b : Books) {
            if (b.getTitle().equalsIgnoreCase(title)) {
                matchingBooks.add(b);
            }
        }
        return matchingBooks;
    }

    public void removeItem(Book book) {
        Books.remove(book);
    }
    
}
