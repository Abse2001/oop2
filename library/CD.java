

public class CD extends Item {
    private String company;
    private int duration;
    private double price;

    public CD(String title, String company, int duration, double price, int quantity) {
        super(title, quantity);
        this.company = company;
        this.duration = duration;
        this.price = price;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toCsvString() {
        return "CD," + title + "," + company + "," + duration + "," + price + "," + quantity + "," + maxQuantity;
    }

    @Override
    public String displayInfo() {
        return String.format("%-30s | %-15s | %3d min | $%8.2f | %d/%d",
            title,
            company,
            duration,
            price,
            quantity,
            maxQuantity);
    }
}
