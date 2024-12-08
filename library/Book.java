public class Book {
    private String title;
    private String category;
    private String author;
    private String email;
    private int quantity;

    public Book(String title, String category, String author, String email, int quantity) {
        this.title = title;
        this.category = category;
        this.author = author;
        this.email = email;
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String displayBookInfo() {
        return this.title + " (" + this.category + ") by " + this.author + " (" + this.email + ") - Quantity: " + this.quantity;
    }
    
}
