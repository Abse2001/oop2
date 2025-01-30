import javax.swing.*;
import java.awt.*;
import java.util.*;

public class LibraryGUI extends JFrame {
    private Library library;
    private JTextArea displayArea;
    private JTextField titleField, authorField, categoryField, emailField, quantityField;
    private JTextField companyField, durationField, priceField;
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

        // Add panels to frame
        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);
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
        companyField = new JTextField(20);
        durationField = new JTextField(5);
        priceField = new JTextField(10);

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
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
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
            String title = titleField.getText();
            int quantity = Integer.parseInt(quantityField.getText());

            if (itemTypeCombo.getSelectedItem().equals("Book")) {
                String category = categoryField.getText();
                String author = authorField.getText();
                String email = emailField.getText();
                Book book = new Book(title, category, author, email, quantity);
                library.addItem(book);
            } else {
                String company = categoryField.getText();
                int duration = Integer.parseInt(authorField.getText());
                double price = Double.parseDouble(emailField.getText());
                CD cd = new CD(title, company, duration, price, quantity);
                library.addItem(cd);
            }
            displayArea.append("Item added successfully\n");
            clearFields();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeItem() {
        String title = titleField.getText();
        String itemType = (String) itemTypeCombo.getSelectedItem();
        
        // Create map for additional search criteria
        Map<String, String> additionalCriteria = new HashMap<>();
        
        if (itemType.equals("Book")) {
            if (!categoryField.getText().trim().isEmpty()) {
                additionalCriteria.put("category", categoryField.getText().trim());
            }
            if (!authorField.getText().trim().isEmpty()) {
                additionalCriteria.put("author", authorField.getText().trim());
            }
            if (!emailField.getText().trim().isEmpty()) {
                additionalCriteria.put("email", emailField.getText().trim());
            }
        } else { // CD
            if (!categoryField.getText().trim().isEmpty()) {
                additionalCriteria.put("company", categoryField.getText().trim());
            }
            if (!authorField.getText().trim().isEmpty()) {
                additionalCriteria.put("duration", authorField.getText().trim());
            }
            if (!emailField.getText().trim().isEmpty()) {
                additionalCriteria.put("price", emailField.getText().trim());
            }
        }
        
        displayArea.setText(""); // Clear previous content
        String result = library.removeItem(title, itemType, additionalCriteria);
        displayArea.append(result);
    }

    private void findItem() {
        String title = titleField.getText();
        String itemType = (String) itemTypeCombo.getSelectedItem();
        
        // Create map for additional search criteria
        Map<String, String> additionalCriteria = new HashMap<>();
        
        if (itemType.equals("Book")) {
            if (!categoryField.getText().trim().isEmpty()) {
                additionalCriteria.put("category", categoryField.getText().trim());
            }
            if (!authorField.getText().trim().isEmpty()) {
                additionalCriteria.put("author", authorField.getText().trim());
            }
            if (!emailField.getText().trim().isEmpty()) {
                additionalCriteria.put("email", emailField.getText().trim());
            }
        } else { // CD
            if (!categoryField.getText().trim().isEmpty()) {
                additionalCriteria.put("company", categoryField.getText().trim());
            }
            if (!authorField.getText().trim().isEmpty()) {
                additionalCriteria.put("duration", authorField.getText().trim());
            }
            if (!emailField.getText().trim().isEmpty()) {
                additionalCriteria.put("price", emailField.getText().trim());
            }
        }
        
        displayArea.setText(""); // Clear previous content
        String results = library.findItem(title, itemType, additionalCriteria);
        displayArea.append(results);
    }

    private void borrowItem() {
        String title = titleField.getText();
        String itemType = (String) itemTypeCombo.getSelectedItem();
        
        // Create map for additional search criteria
        Map<String, String> additionalCriteria = new HashMap<>();
        
        if (itemType.equals("Book")) {
            if (!categoryField.getText().trim().isEmpty()) {
                additionalCriteria.put("category", categoryField.getText().trim());
            }
            if (!authorField.getText().trim().isEmpty()) {
                additionalCriteria.put("author", authorField.getText().trim());
            }
            if (!emailField.getText().trim().isEmpty()) {
                additionalCriteria.put("email", emailField.getText().trim());
            }
        } else { // CD
            if (!categoryField.getText().trim().isEmpty()) {
                additionalCriteria.put("company", categoryField.getText().trim());
            }
            if (!authorField.getText().trim().isEmpty()) {
                additionalCriteria.put("duration", authorField.getText().trim());
            }
            if (!emailField.getText().trim().isEmpty()) {
                additionalCriteria.put("price", emailField.getText().trim());
            }
        }
        
        displayArea.setText(""); // Clear previous content
        String result = library.borrowItem(title, itemType, additionalCriteria);
        displayArea.append(result);
    }

    private void returnItem() {
        String title = titleField.getText();
        String itemType = (String) itemTypeCombo.getSelectedItem();
        
        // Create map for additional search criteria
        Map<String, String> additionalCriteria = new HashMap<>();
        
        if (itemType.equals("Book")) {
            if (!categoryField.getText().trim().isEmpty()) {
                additionalCriteria.put("category", categoryField.getText().trim());
            }
            if (!authorField.getText().trim().isEmpty()) {
                additionalCriteria.put("author", authorField.getText().trim());
            }
            if (!emailField.getText().trim().isEmpty()) {
                additionalCriteria.put("email", emailField.getText().trim());
            }
        } else { // CD
            if (!categoryField.getText().trim().isEmpty()) {
                additionalCriteria.put("company", categoryField.getText().trim());
            }
            if (!authorField.getText().trim().isEmpty()) {
                additionalCriteria.put("duration", authorField.getText().trim());
            }
            if (!emailField.getText().trim().isEmpty()) {
                additionalCriteria.put("price", emailField.getText().trim());
            }
        }
        
        displayArea.setText(""); // Clear previous content
        String result = library.returnItem(title, itemType, additionalCriteria);
        displayArea.append(result);
    }

    private void listAll() {
        displayArea.setText(""); // Clear previous content
        for (Item item : library.getItems()) {
            displayArea.append(item.displayInfo() + "\n");
        }
        if (library.getItems().isEmpty()) {
            displayArea.append("The library is empty\n");
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
        SwingUtilities.invokeLater(() -> {
            LibraryGUI gui = new LibraryGUI();
            gui.setVisible(true);
        });
    }
}
