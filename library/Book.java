public class Book {
    private String title;
    private String category;
    private String author;
    private String email;
    private int quantity;
    private int maxQuantity;

    public Book(String title, String category, String author, String email, int quantity) {
        this.title = title;
        this.category = category;
        this.author = author;
        this.email = email;
        this.quantity = quantity;
        this.maxQuantity = quantity;
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
    public int getMaxQuantity() {
        return maxQuantity;
    }
    public void setMaxQuantity(int maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public String displayBookInfo() {
        return "Title: " + this.title + ", Category: [" + this.category + "], By: " + this.author + ", Email: (" + this.email + ") - Quantity: " + this.quantity;
    }
    
}
