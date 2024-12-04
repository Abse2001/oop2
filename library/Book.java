public class Book {
    private String title;
    private String author;
    private String email;
    private int quantity;

    public Book(String title, String author, String email, int quantity) {
        this.title = title;
        this.author = author;
        this.email = email;
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getEmail() {
        return email;
    }

    public int getQuantity() {
        return quantity;
    }

    public void increaseQuantity(int amount) {
        this.quantity += amount;
    }

    public void decreaseQuantity() {
        this.quantity--;
    }

    @Override
    public String toString() {
        return title + "   " + author + "   " + email;
    }
}
