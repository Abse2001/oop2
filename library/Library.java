import java.util.*;

public class Library {
    private ArrayList<Book> Books;

    public Library() {
        Books = new ArrayList<Book>();
    }
    public void addItem(Book book) {
        for (Book b : Books) {
            if (b.getTitle().equalsIgnoreCase(book.getTitle()) && b.getAuthor().equalsIgnoreCase(book.getAuthor()) && b.getEmail().equalsIgnoreCase(book.getEmail()) && b.getCategory().equalsIgnoreCase(book.getCategory())) {
                b.setQuantity(b.getQuantity() + book.getQuantity());
                b.setMaxQuantity( b.getMaxQuantity() + book.getQuantity());
                System.out.println("Book : " + book.getTitle() + " , " + book.getCategory() + "increased max quantity to : " + b.getMaxQuantity());
                return;
            }
        }
        System.out.println("Book : " + book.displayBookInfo() + " added successfully.");
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

    public boolean findItem(String title) {
        boolean found = false;
        for (Book b : Books) {
            if (b.getTitle().toLowerCase().contains(title.toLowerCase()) || b.getAuthor().toLowerCase().contains(title.toLowerCase()) || b.getCategory().toLowerCase().contains(title.toLowerCase()) || b.getEmail().toLowerCase().contains(title.toLowerCase())) {
                System.out.println(b.displayBookInfo());
                found = true;
            }
        }
        if (found) {
            return true;
        }
        System.out.println("No book matches the title: " + title);
        return false;
    }
    

    public void borrowItem(Book book) {
      
            if (book.getQuantity() > 0) {
                book.setQuantity(book.getQuantity() - 1);
                System.out.println("Book borrowed: " + book.displayBookInfo());

            } else {
                System.out.println("No available item");
            }
        
    }

    public void returnItem(Book book) {
        if (book.getQuantity() < book.getMaxQuantity()) {
            book.setQuantity(book.getQuantity() + 1);
            System.out.println("Done");
        }else{
            System.out.println("The stock is full");
            System.out.println("Use add command with the book details to increase the stock.");
        }
    }

    public void listAllItems() {
        if(!Books.isEmpty()){
        for (Book book : Books) {
           System.out.println(book.displayBookInfo());
        }
    }else{
        System.out.println("The library is empty");
    }
    }
    public ArrayList<Book> findItems(String title,String author) {
        ArrayList<Book> matchingBooks = new ArrayList<>();
        for (Book b : Books) {
            if (b.getTitle().equalsIgnoreCase(title) && b.getAuthor().equalsIgnoreCase(author)) {
                matchingBooks.add(b);
            }
        }
        return matchingBooks;
    }

    public void removeItem(Book book) {
        Books.remove(book);
    }
    
}
