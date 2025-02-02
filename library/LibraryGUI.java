import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class LibraryGUI extends JFrame {
    private Library library;
    private JTextArea displayArea;
    private JTable resultTable;
    private JScrollPane tableScrollPane;
    private JTextField titleField, authorField, categoryField, emailField, quantityField;
    private JComboBox<String> itemTypeCombo;

    public LibraryGUI() {
        library = new Library();
        setTitle("Library Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create panels
        JPanel inputPanel = createInputPanel();
        JPanel buttonPanel = createButtonPanel();
        createDisplayArea();
        createResultTable();

        // Add panels to frame
        add(inputPanel, BorderLayout.NORTH);
        JPanel displayPanel = new JPanel(new BorderLayout());
        displayPanel.add(new JScrollPane(displayArea), BorderLayout.NORTH);
        displayPanel.add(tableScrollPane, BorderLayout.CENTER);
        add(displayPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Item type selection
        itemTypeCombo = new JComboBox<>(new String[]{"Book", "CD"});
        titleField = new JTextField(20);
        authorField = new JTextField(20);
        categoryField = new JTextField(20);
        emailField = new JTextField(20);
        quantityField = new JTextField(5);
        // Add components with labels
        JLabel itemTypeLabel = new JLabel("Item Type:");
        addComponent(panel, itemTypeLabel, itemTypeCombo, gbc, 0);
        addComponent(panel, new JLabel("Title:"), titleField, gbc, 1);
        addComponent(panel, new JLabel("Category:"), categoryField, gbc, 2);
        addComponent(panel, new JLabel("Author:"), authorField, gbc, 3);
        addComponent(panel, new JLabel("Email:"), emailField, gbc, 4);
        addComponent(panel, new JLabel("Quantity:"), quantityField, gbc, 5);

        itemTypeCombo.addActionListener(e -> updateFieldsVisibility());
        updateFieldsVisibility();

        return panel;
    }

    private void addComponent(JPanel panel, JLabel label, JComponent component, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(label, gbc);
        gbc.gridx = 1;
        panel.add(component, gbc);
    }

    private void updateFieldsVisibility() {
        boolean isBook = itemTypeCombo.getSelectedItem().equals("Book");
        
        // Get all labels from the panel
        Container panel = categoryField.getParent();
        Component[] components = panel.getComponents();
        
        // Find and update the labels
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof JLabel) {
                JLabel label = (JLabel) components[i];
                String labelText = label.getText();
                
                if (labelText.equals("Category:") || labelText.equals("Company:")) {
                    label.setText(isBook ? "Category:" : "Company:");
                } else if (labelText.equals("Author:") || labelText.equals("Duration (min):")) {
                    label.setText(isBook ? "Author:" : "Duration (min):");
                } else if (labelText.equals("Email:") || labelText.equals("Price ($):")) {
                    label.setText(isBook ? "Email:" : "Price ($):");
                }
            }
        }

        // Clear fields when switching
        categoryField.setText("");
        authorField.setText("");
        emailField.setText("");
        quantityField.setText("");
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        String[] buttonLabels = {"Add Item", "Remove Item", "Find Item", "Borrow Item", "Return Item", "List All"};
        
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.addActionListener(e -> handleButtonClick(label));
            panel.add(button);
        }
        
        return panel;
    }

    private void createDisplayArea() {
        displayArea = new JTextArea(5, 50);  // Reduced height since we have table below
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        displayArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        displayArea.setLineWrap(true);
        displayArea.setWrapStyleWord(true);
        displayArea.setText("Welcome to Library Management System!\nUse the controls above to manage items.");
    }

    private void appendItemDetails(Item item) {
        DefaultTableModel model = (DefaultTableModel) resultTable.getModel();
        
        if (item instanceof Book book) {
            model.addRow(new Object[]{
                "Book",
                book.getTitle(),
                book.getCategory(),
                book.getAuthor(),
                book.getEmail(),
                String.format("%d/%d", book.getQuantity(), book.getMaxQuantity())
            });
        } else if (item instanceof CD cd) {
            model.addRow(new Object[]{
                "CD",
                cd.getTitle(),
                cd.getCompany(),
                cd.getDuration() + " min",
                String.format("$%.2f", cd.getPrice()),
                String.format("%d/%d", cd.getQuantity(), cd.getMaxQuantity())
            });
        }
    }

private void createResultTable() {
    String[] columnNames = {"Type", "Title", "Category/Company", "Author/Duration", "Email/Price", "Stock"};
    
    DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Make table read-only
        }
    };

    resultTable = new JTable(model);
    resultTable.setFillsViewportHeight(true);
    resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

    // 🚀 FULLY DISABLE EDITING
    resultTable.setDefaultEditor(Object.class, null); // <--- Add this!

    // Set column widths
    int[] columnWidths = {60, 150, 100, 100, 100, 80};
    for (int i = 0; i < columnWidths.length; i++) {
        resultTable.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
    }

    tableScrollPane = new JScrollPane(resultTable);
    tableScrollPane.setPreferredSize(new Dimension(600, 300));
}



    private void handleButtonClick(String command) {
        try {
            switch (command) {
                case "Add Item":
                    addItem();
                    break;
                case "Remove Item":
                    removeItem();
                    break;
                case "Find Item":
                    findItem();
                    break;
                case "Borrow Item":
                    borrowItem();
                    break;
                case "Return Item":
                    returnItem();
                    break;
                case "List All":
                    listAll();
                    break;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addItem() {
        try {
            // Validate title
            String title = titleField.getText().trim();
            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Title field cannot be empty\n" +
                    "Required: Text (e.g., 'Java Programming')", 
                    "Title Validation Error", JOptionPane.ERROR_MESSAGE);
                titleField.requestFocus();
                return;
            }

            // Validate quantity
            String quantityStr = quantityField.getText().trim();
            if (quantityStr.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Quantity field cannot be empty\n" +
                    "Required: Positive whole number (e.g., '5')",
                    "Quantity Validation Error", JOptionPane.ERROR_MESSAGE);
                quantityField.requestFocus();
                return;
            }

            int quantity;
            try {
                quantity = Integer.parseInt(quantityStr);
                if (quantity <= 0) {
                    JOptionPane.showMessageDialog(this,
                        "Invalid quantity value\n" +
                        "Required: Number greater than 0\n" +
                        "Current value: " + quantityStr,
                        "Quantity Validation Error", JOptionPane.ERROR_MESSAGE);
                    quantityField.requestFocus();
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                    "Invalid quantity format\n" +
                    "Required: Whole number (e.g., '5')\n" +
                    "Current value: " + quantityStr,
                    "Quantity Validation Error", JOptionPane.ERROR_MESSAGE);
                quantityField.requestFocus();
                return;
            }

            if (itemTypeCombo.getSelectedItem().equals("Book")) {
                String category = categoryField.getText().trim();
                String author = authorField.getText().trim();
                String email = emailField.getText().trim();

                // Validate book-specific fields
                if (category.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "Category field cannot be empty\n" +
                        "Required: Text (e.g., 'Computer Science')",
                        "Category Validation Error", JOptionPane.ERROR_MESSAGE);
                    categoryField.requestFocus();
                    return;
                }

                if (author.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "Author field cannot be empty\n" +
                        "Required: Text (e.g., 'John Smith')",
                        "Author Validation Error", JOptionPane.ERROR_MESSAGE);
                    authorField.requestFocus();
                    return;
                }

                if (email.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "Email field cannot be empty\n" +
                        "Required: Valid email address (e.g., 'author@example.com')",
                        "Email Validation Error", JOptionPane.ERROR_MESSAGE);
                    emailField.requestFocus();
                    return;
                }

                if (!email.contains("@") || !email.contains(".") || email.indexOf("@") > email.lastIndexOf(".")) {
                    JOptionPane.showMessageDialog(this,
                        "Invalid email address format\n" +
                        "Required: Valid email (e.g., 'author@example.com')\n" +
                        "Current value: " + email,
                        "Email Validation Error", JOptionPane.ERROR_MESSAGE);
                    emailField.requestFocus();
                    return;
                }

                Book book = new Book(title, category, author, email, quantity);
                library.addItem(book);
            } else {
                String company = categoryField.getText().trim();
                String durationStr = authorField.getText().trim();
                String priceStr = emailField.getText().trim();

                // Validate CD-specific fields
                if (company.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "Company field cannot be empty\n" +
                        "Required: Text (e.g., 'Sony Music')",
                        "Company Validation Error", JOptionPane.ERROR_MESSAGE);
                    categoryField.requestFocus();
                    return;
                }

                if (durationStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "Duration field cannot be empty\n" +
                        "Required: Positive whole number in minutes (e.g., '45')",
                        "Duration Validation Error", JOptionPane.ERROR_MESSAGE);
                    authorField.requestFocus();
                    return;
                }

                int duration;
                try {
                    duration = Integer.parseInt(durationStr);
                    if (duration <= 0) {
                        JOptionPane.showMessageDialog(this,
                            "Invalid duration value\n" +
                            "Required: Number greater than 0\n" +
                            "Current value: " + durationStr + " minutes",
                            "Duration Validation Error", JOptionPane.ERROR_MESSAGE);
                        authorField.requestFocus();
                        return;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this,
                        "Invalid duration format\n" +
                        "Required: Whole number in minutes (e.g., '45')\n" +
                        "Current value: " + durationStr,
                        "Duration Validation Error", JOptionPane.ERROR_MESSAGE);
                    authorField.requestFocus();
                    return;
                }

                if (priceStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "Price field cannot be empty\n" +
                        "Required: Positive number with optional decimals (e.g., '12.99')",
                        "Price Validation Error", JOptionPane.ERROR_MESSAGE);
                    emailField.requestFocus();
                    return;
                }

                double price;
                try {
                    price = Double.parseDouble(priceStr);
                    if (price <= 0) {
                        JOptionPane.showMessageDialog(this,
                            "Invalid price value\n" +
                            "Required: Number greater than 0\n" +
                            "Current value: $" + priceStr,
                            "Price Validation Error", JOptionPane.ERROR_MESSAGE);
                        emailField.requestFocus();
                        return;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this,
                        "Invalid price format\n" +
                        "Required: Number with optional decimals (e.g., '12.99')\n" +
                        "Current value: " + priceStr,
                        "Price Validation Error", JOptionPane.ERROR_MESSAGE);
                    emailField.requestFocus();
                    return;
                }

                CD cd = new CD(title, company, duration, price, quantity);
                library.addItem(cd);
                displayArea.setText("");
                appendItemDetails(cd);
            }
            
            clearFields();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + e.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeItem() {
        String title = titleField.getText().trim();
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title field cannot be empty", "Validation Error", JOptionPane.ERROR_MESSAGE);
            titleField.requestFocus();
            return;
        }

        String itemType = itemTypeCombo.getSelectedItem().toString();
        
        String author = null;
        String category = null;
        String email = null;
        String company = null;
        String duration = null;
        String price = null;
        
        if (itemType.equals("Book")) {
            category = categoryField.getText().trim();
            author = authorField.getText().trim();
            email = emailField.getText().trim();
        } else { // CD
            company = categoryField.getText().trim();
            duration = authorField.getText().trim();
            price = emailField.getText().trim();
        }
        
        displayArea.setText(""); // Clear previous content
        String result = library.removeItem(title, itemType, author, category, email, company, duration, price);
        displayArea.append(result);
    }

    private void findItem() {
        String title = titleField.getText().trim();
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title field cannot be empty", "Validation Error", JOptionPane.ERROR_MESSAGE);
            titleField.requestFocus();
            return;
        }

        String itemType = itemTypeCombo.getSelectedItem().toString();
        
        String author = null;
        String category = null;
        String email = null;
        String company = null;
        String duration = null;
        String price = null;
        
        if (itemType.equals("Book")) {
            category = categoryField.getText().trim();
            author = authorField.getText().trim();
            email = emailField.getText().trim();
        } else { // CD
            company = categoryField.getText().trim();
            duration = authorField.getText().trim();
            price = emailField.getText().trim();
        }
        
        displayArea.setText(""); // Clear previous content
        String result = library.findItem(title, itemType, author, category, email, company, duration, price);
        displayArea.append(result);

        // Clear previous table content
        DefaultTableModel model = (DefaultTableModel) resultTable.getModel();
        model.setRowCount(0);

        // Populate table with found items
        ArrayList<Item> foundItems = library.findItems(title, itemType, new HashMap<>());
        if (foundItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No items found matching the search criteria", "Search Result", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (Item item : foundItems) {
                appendItemDetails(item);
            }
        }
    }

    private void borrowItem() {
        String title = titleField.getText().trim();
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title field cannot be empty", "Validation Error", JOptionPane.ERROR_MESSAGE);
            titleField.requestFocus();
            return;
        }

        String itemType = itemTypeCombo.getSelectedItem().toString();
        
        String author = null;
        String category = null;
        String email = null;
        String company = null;
        String duration = null;
        String price = null;
        
        if (itemType.equals("Book")) {
            category = categoryField.getText().trim();
            author = authorField.getText().trim();
            email = emailField.getText().trim();
        } else { // CD
            company = categoryField.getText().trim();
            duration = authorField.getText().trim();
            price = emailField.getText().trim();
        }
        
        displayArea.setText(""); // Clear previous content
        String result = library.borrowItem(title, itemType, author, category, email, company, duration, price);
        displayArea.append(result);
    }

    private void returnItem() {
        String title = titleField.getText().trim();
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title field cannot be empty", "Validation Error", JOptionPane.ERROR_MESSAGE);
            titleField.requestFocus();
            return;
        }

        String itemType = itemTypeCombo.getSelectedItem().toString();
        
        String author = null;
        String category = null;
        String email = null;
        String company = null;
        String duration = null;
        String price = null;
        
        if (itemType.equals("Book")) {
            category = categoryField.getText().trim();
            author = authorField.getText().trim();
            email = emailField.getText().trim();
        } else { // CD
            company = categoryField.getText().trim();
            duration = authorField.getText().trim();
            price = emailField.getText().trim();
        }
        
        displayArea.setText(""); // Clear previous content
        String result = library.returnItem(title, itemType, author, category, email, company, duration, price);
        displayArea.append(result);
    }

    private void listAll() {
        ArrayList<Item> items = library.getItems();
        
        if (items.isEmpty()) {
            JOptionPane.showMessageDialog(this, "The library is empty", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Separate books and CDs
        ArrayList<Item> books = new ArrayList<>();
        ArrayList<Item> cds = new ArrayList<>();
        
        for (Item item : items) {
            if (item instanceof Book) {
                books.add(item);
            } else if (item instanceof CD) {
                cds.add(item);
            }
        }

        // Clear previous content
        DefaultTableModel model = new DefaultTableModel();
        resultTable.setModel(model);

        if (!books.isEmpty()) {
            model = new DefaultTableModel(new String[]{
                "Type", "Title", "Category", "Author", "Email", "Stock"
            }, 0);
            resultTable.setModel(model);
            
            for (Item item : books) {
                appendItemDetails(item);
            }
        }

        if (!cds.isEmpty()) {
            if (books.isEmpty()) {
                model = new DefaultTableModel(new String[]{
                    "Type", "Title", "Company", "Duration", "Price", "Stock"
                }, 0);
                resultTable.setModel(model);
            }
            
            for (Item item : cds) {
                appendItemDetails(item);
            }
        }

        // Adjust column widths
        for (int i = 0; i < resultTable.getColumnCount(); i++) {
            TableColumn column = resultTable.getColumnModel().getColumn(i);
            column.setPreferredWidth(100);
        }
    }

    private void clearFields() {
        titleField.setText("");
        categoryField.setText("");
        authorField.setText("");
        emailField.setText("");
        quantityField.setText("");
    }

    public static void main(String[] args) {
       
            LibraryGUI gui = new LibraryGUI();
            gui.setVisible(true);
    
    }
}
