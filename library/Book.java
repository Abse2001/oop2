

public class Book extends Item {
    private String category;
    private String author;
    private String email;

    public Book(String title, String category, String author, String email, int quantity) {
        super(title, quantity);
        this.category = category;
        this.author = author;
        this.email = email;
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

    @Override
    public String toCsvString() {
        return "BOOK," + title + "," + category + "," + author + "," + email + "," + quantity + "," + maxQuantity;
    }

    @Override
    public String displayInfo() {
        return String.format("%-30s | %-15s | %-20s | %-25s | %d/%d", 
            title,
            category,
            author,
            email,
            quantity,
            maxQuantity);
        
    }
}
