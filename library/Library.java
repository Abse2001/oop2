

import java.util.*;
import java.io.*;

public class Library {
    private ArrayList<Item> items;
    private static final String DATA_FILE = "library_data.txt";

    public Library() {
        items = new ArrayList<Item>();
        loadFromFile();
    }

    private void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals("BOOK")) {
                    Book book = new Book(parts[1], parts[2], parts[3], parts[4], Integer.parseInt(parts[5]));
                    book.setMaxQuantity(Integer.parseInt(parts[6]));
                    items.add(book);
                } else if (parts[0].equals("CD")) {
                    CD cd = new CD(parts[1], parts[2], Integer.parseInt(parts[3]), 
                          Double.parseDouble(parts[4]), Integer.parseInt(parts[5]));
                    cd.setMaxQuantity(Integer.parseInt(parts[6]));
                    items.add(cd);
                }
            }
        } catch (IOException e) {
            System.out.println("No existing data file found. Starting with empty library.");
        }
    }

    public void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (Item item : items) {
                writer.println(item.toCsvString());
            }
        } catch (IOException e) {
            System.out.println("Error saving library data: " + e.getMessage());
        }
    }
    public void addItem(Item item) {
        for (Item existingItem : items) {
            if (existingItem.getTitle().equalsIgnoreCase(item.getTitle()) && 
                existingItem.getClass() == item.getClass()) {
                if (item instanceof Book) {
                    Book existingBook = (Book) existingItem;
                    Book newBook = (Book) item;
                    if (existingBook.getAuthor().equalsIgnoreCase(newBook.getAuthor())) {
                        int newQuantity = existingItem.getQuantity() + item.getQuantity();
                        int newMaxQuantity = existingItem.getMaxQuantity() + item.getQuantity();
                        existingItem.setQuantity(newQuantity);
                        existingItem.setMaxQuantity(newMaxQuantity);
                        System.out.println("Book: " + item.getTitle() + " increased max quantity to: " + existingItem.getMaxQuantity());
                        saveToFile();
                        return;
                    }
                } else if (item instanceof CD) {
                    CD existingCD = (CD) existingItem;
                    CD newCD = (CD) item;
                    if (existingCD.getCompany().equalsIgnoreCase(newCD.getCompany())) {
                        existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                        existingItem.setMaxQuantity(existingItem.getMaxQuantity() + item.getQuantity());
                        System.out.println("CD: " + item.getTitle() + " increased max quantity to: " + existingItem.getMaxQuantity());
                        saveToFile();
                        return;
                    }
                }
            }
        }
        items.add(item);
        System.out.println("Item: " + item.displayInfo() + " added successfully.");
        saveToFile();
    }

    public String removeItem(String title, String itemType, Map<String, String> additionalCriteria) {
        if (title == null || title.trim().isEmpty()) {
            return "Error: Title is required for removal";
        }

        List<Item> matchingItems = new ArrayList<>();
        Iterator<Item> iterator = items.iterator();
        
        while (iterator.hasNext()) {
            Item item = iterator.next();
            if (item.getTitle().equalsIgnoreCase(title)) {
                if ((itemType.equals("Book") && item instanceof Book) || 
                    (itemType.equals("CD") && item instanceof CD)) {
                    
                    boolean meetsAllCriteria = true;
                    if (additionalCriteria != null && !additionalCriteria.isEmpty()) {
                        for (Map.Entry<String, String> criterion : additionalCriteria.entrySet()) {
                            String key = criterion.getKey().toLowerCase();
                            String value = criterion.getValue().toLowerCase();
                            
                            if (value != null && !value.isEmpty()) {
                                if (item instanceof Book) {
                                    Book book = (Book) item;
                                    switch (key) {
                                        case "author":
                                            if (!book.getAuthor().toLowerCase().contains(value)) 
                                                meetsAllCriteria = false;
                                            break;
                                        case "category":
                                            if (!book.getCategory().toLowerCase().contains(value)) 
                                                meetsAllCriteria = false;
                                            break;
                                        case "email":
                                            if (!book.getEmail().toLowerCase().contains(value)) 
                                                meetsAllCriteria = false;
                                            break;
                                    }
                                } else if (item instanceof CD) {
                                    CD cd = (CD) item;
                                    switch (key) {
                                        case "company":
                                            if (!cd.getCompany().toLowerCase().contains(value)) 
                                                meetsAllCriteria = false;
                                            break;
                                        case "duration":
                                            try {
                                                if (cd.getDuration() != Integer.parseInt(value)) 
                                                    meetsAllCriteria = false;
                                            } catch (NumberFormatException e) {
                                                meetsAllCriteria = false;
                                            }
                                            break;
                                        case "price":
                                            try {
                                                if (cd.getPrice() != Double.parseDouble(value)) 
                                                    meetsAllCriteria = false;
                                            } catch (NumberFormatException e) {
                                                meetsAllCriteria = false;
                                            }
                                            break;
                                    }
                                }
                            }
                        }
                    }
                    
                    if (meetsAllCriteria) {
                        matchingItems.add(item);
                    }
                }
            }
        }

        if (matchingItems.isEmpty()) {
            return "No items found matching the removal criteria";
        } else if (matchingItems.size() > 1 && (additionalCriteria == null || additionalCriteria.isEmpty())) {
            StringBuilder result = new StringBuilder("Multiple matching items found:\n");
            for (Item item : matchingItems) {
                result.append(item.displayInfo()).append("\n");
            }
            result.append("\nPlease provide additional criteria to identify the specific item to remove.");
            return result.toString();
        } else if (matchingItems.size() == 1) {
            items.remove(matchingItems.get(0));
            saveToFile();
            return "Item removed successfully:\n" + matchingItems.get(0).displayInfo();
        }
        
        // If we get here, we have multiple matches even with criteria
        return "Multiple items still match the criteria. Please provide more specific information.";
    }

    public String findItem(String title, String itemType, Map<String, String> additionalCriteria) {
        if (title == null || title.trim().isEmpty()) {
            return "Error: Title is required for search";
        }

        StringBuilder results = new StringBuilder();
        boolean found = false;
        int matchCount = 0;

        for (Item item : items) {
            boolean matches = item.getTitle().toLowerCase().contains(title.toLowerCase());
            
            if ((itemType.equals("Book") && item instanceof Book) || 
                (itemType.equals("CD") && item instanceof CD)) {
                
                if (matches) {
                    boolean meetsAllCriteria = true;
                    
                    if (additionalCriteria != null && !additionalCriteria.isEmpty()) {
                        for (Map.Entry<String, String> criterion : additionalCriteria.entrySet()) {
                            String key = criterion.getKey().toLowerCase();
                            String value = criterion.getValue().toLowerCase();
                            
                            if (value != null && !value.isEmpty()) {
                                if (item instanceof Book) {
                                    Book book = (Book) item;
                                    switch (key) {
                                        case "author":
                                            if (!book.getAuthor().toLowerCase().contains(value)) 
                                                meetsAllCriteria = false;
                                            break;
                                        case "category":
                                            if (!book.getCategory().toLowerCase().contains(value)) 
                                                meetsAllCriteria = false;
                                            break;
                                        case "email":
                                            if (!book.getEmail().toLowerCase().contains(value)) 
                                                meetsAllCriteria = false;
                                            break;
                                    }
                                } else if (item instanceof CD) {
                                    CD cd = (CD) item;
                                    switch (key) {
                                        case "company":
                                            if (!cd.getCompany().toLowerCase().contains(value)) 
                                                meetsAllCriteria = false;
                                            break;
                                        case "duration":
                                            try {
                                                if (cd.getDuration() != Integer.parseInt(value)) 
                                                    meetsAllCriteria = false;
                                            } catch (NumberFormatException e) {
                                                meetsAllCriteria = false;
                                            }
                                            break;
                                        case "price":
                                            try {
                                                if (cd.getPrice() != Double.parseDouble(value)) 
                                                    meetsAllCriteria = false;
                                            } catch (NumberFormatException e) {
                                                meetsAllCriteria = false;
                                            }
                                            break;
                                    }
                                }
                            }
                        }
                    }
                    
                    if (meetsAllCriteria) {
                        results.append(item.displayInfo()).append("\n");
                        found = true;
                        matchCount++;
                    }
                }
            }
        }

        if (!found) {
            return "No items found matching the search criteria";
        } else if (matchCount > 1) {
            return "Multiple matches found:\n" + results.toString() + 
                   "\nPlease provide additional search criteria to narrow down results.";
        }
        
        return results.toString();
    }

    public String borrowItem(String title, String itemType, Map<String, String> additionalCriteria) {
        if (title == null || title.trim().isEmpty()) {
            return "Error: Title is required for borrowing";
        }

        List<Item> matchingItems = new ArrayList<>();
        for (Item item : items) {
            if (item.getTitle().equalsIgnoreCase(title)) {
                if ((itemType.equals("Book") && item instanceof Book) || 
                    (itemType.equals("CD") && item instanceof CD)) {
                    
                    boolean meetsAllCriteria = true;
                    if (additionalCriteria != null && !additionalCriteria.isEmpty()) {
                        for (Map.Entry<String, String> criterion : additionalCriteria.entrySet()) {
                            String key = criterion.getKey().toLowerCase();
                            String value = criterion.getValue().toLowerCase();
                            
                            if (value != null && !value.isEmpty()) {
                                if (item instanceof Book) {
                                    Book book = (Book) item;
                                    switch (key) {
                                        case "author":
                                            if (!book.getAuthor().toLowerCase().contains(value)) 
                                                meetsAllCriteria = false;
                                            break;
                                        case "category":
                                            if (!book.getCategory().toLowerCase().contains(value)) 
                                                meetsAllCriteria = false;
                                            break;
                                        case "email":
                                            if (!book.getEmail().toLowerCase().contains(value)) 
                                                meetsAllCriteria = false;
                                            break;
                                    }
                                } else if (item instanceof CD) {
                                    CD cd = (CD) item;
                                    switch (key) {
                                        case "company":
                                            if (!cd.getCompany().toLowerCase().contains(value)) 
                                                meetsAllCriteria = false;
                                            break;
                                        case "duration":
                                            try {
                                                if (cd.getDuration() != Integer.parseInt(value)) 
                                                    meetsAllCriteria = false;
                                            } catch (NumberFormatException e) {
                                                meetsAllCriteria = false;
                                            }
                                            break;
                                        case "price":
                                            try {
                                                if (cd.getPrice() != Double.parseDouble(value)) 
                                                    meetsAllCriteria = false;
                                            } catch (NumberFormatException e) {
                                                meetsAllCriteria = false;
                                            }
                                            break;
                                    }
                                }
                            }
                        }
                    }
                    
                    if (meetsAllCriteria) {
                        matchingItems.add(item);
                    }
                }
            }
        }

        if (matchingItems.isEmpty()) {
            return "No items found matching the borrowing criteria";
        } else if (matchingItems.size() > 1 && (additionalCriteria == null || additionalCriteria.isEmpty())) {
            StringBuilder result = new StringBuilder("Multiple matching items found:\n");
            for (Item item : matchingItems) {
                result.append(item.displayInfo()).append("\n");
            }
            result.append("\nPlease provide additional criteria to identify the specific item to borrow.");
            return result.toString();
        } else if (matchingItems.size() == 1) {
            Item item = matchingItems.get(0);
            if (item.getQuantity() > 0) {
                item.setQuantity(item.getQuantity() - 1);
                saveToFile();
                return "Item borrowed successfully:\n" + item.displayInfo() + 
                       "\nCurrent quantity: " + item.getQuantity() + "/" + item.getMaxQuantity();
            } else {
                return "No available copies of:\n" + item.displayInfo();
            }
        }
        
        return "Multiple items still match the criteria. Please provide more specific information.";
    }

    public String returnItem(String title, String itemType, Map<String, String> additionalCriteria) {
        if (title == null || title.trim().isEmpty()) {
            return "Error: Title is required for returning";
        }

        List<Item> matchingItems = new ArrayList<>();
        for (Item item : items) {
            if (item.getTitle().equalsIgnoreCase(title)) {
                if ((itemType.equals("Book") && item instanceof Book) || 
                    (itemType.equals("CD") && item instanceof CD)) {
                    
                    boolean meetsAllCriteria = true;
                    if (additionalCriteria != null && !additionalCriteria.isEmpty()) {
                        for (Map.Entry<String, String> criterion : additionalCriteria.entrySet()) {
                            String key = criterion.getKey().toLowerCase();
                            String value = criterion.getValue().toLowerCase();
                            
                            if (value != null && !value.isEmpty()) {
                                if (item instanceof Book) {
                                    Book book = (Book) item;
                                    switch (key) {
                                        case "author":
                                            if (!book.getAuthor().toLowerCase().contains(value)) 
                                                meetsAllCriteria = false;
                                            break;
                                        case "category":
                                            if (!book.getCategory().toLowerCase().contains(value)) 
                                                meetsAllCriteria = false;
                                            break;
                                        case "email":
                                            if (!book.getEmail().toLowerCase().contains(value)) 
                                                meetsAllCriteria = false;
                                            break;
                                    }
                                } else if (item instanceof CD) {
                                    CD cd = (CD) item;
                                    switch (key) {
                                        case "company":
                                            if (!cd.getCompany().toLowerCase().contains(value)) 
                                                meetsAllCriteria = false;
                                            break;
                                        case "duration":
                                            try {
                                                if (cd.getDuration() != Integer.parseInt(value)) 
                                                    meetsAllCriteria = false;
                                            } catch (NumberFormatException e) {
                                                meetsAllCriteria = false;
                                            }
                                            break;
                                        case "price":
                                            try {
                                                if (cd.getPrice() != Double.parseDouble(value)) 
                                                    meetsAllCriteria = false;
                                            } catch (NumberFormatException e) {
                                                meetsAllCriteria = false;
                                            }
                                            break;
                                    }
                                }
                            }
                        }
                    }
                    
                    if (meetsAllCriteria) {
                        matchingItems.add(item);
                    }
                }
            }
        }

        if (matchingItems.isEmpty()) {
            return "No items found matching the return criteria";
        } else if (matchingItems.size() > 1 && (additionalCriteria == null || additionalCriteria.isEmpty())) {
            StringBuilder result = new StringBuilder("Multiple matching items found:\n");
            for (Item item : matchingItems) {
                result.append(item.displayInfo()).append("\n");
            }
            result.append("\nPlease provide additional criteria to identify the specific item to return.");
            return result.toString();
        } else if (matchingItems.size() == 1) {
            Item item = matchingItems.get(0);
            int currentMaxQuantity = item.getMaxQuantity();
            if (item.getQuantity() < currentMaxQuantity) {
                item.setQuantity(item.getQuantity() + 1);
                saveToFile();
                return "Item returned successfully:\n" + item.displayInfo() + 
                       "\nCurrent quantity: " + item.getQuantity() + "/" + currentMaxQuantity;
            } else {
                return "Cannot return item (maximum quantity reached):\n" + item.displayInfo() + 
                       "\nCurrent quantity: " + item.getQuantity() + "/" + currentMaxQuantity;
            }
        }
        
        return "Multiple items still match the criteria. Please provide more specific information.";
    }

    public void listAllItems() {
        if (items.isEmpty()) {
            System.out.println("The library is empty");
            return;
        }
        
        for (Item item : items) {
            System.out.println(item.displayInfo());
        }
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public Item findItemByTitle(String title, String itemType) {
        for (Item item : items) {
            if (item.getTitle().equalsIgnoreCase(title)) {
                if ((itemType.equals("Book") && item instanceof Book) || 
                    (itemType.equals("CD") && item instanceof CD)) {
                    return item;
                }
            }
        }
        return null;
    }
    
}
