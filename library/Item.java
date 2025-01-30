

public abstract class Item {
    protected String title;
    protected int quantity;
    protected int maxQuantity;

    public Item(String title, int quantity) {
        this.title = title;
        this.quantity = quantity;
        this.maxQuantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public abstract String toCsvString();
    public abstract String displayInfo();
}
