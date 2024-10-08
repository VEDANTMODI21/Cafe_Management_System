package src;

import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GenerateBill extends JFrame {

    JTextField food, quantity;
    String[] columnNames = {"Food Name", "Quantity", "Price"};
    JTable cart;
    JLabel totalP = new JLabel("Total Price: 0.0 tk");
    DefaultTableModel model;
    double totalprice = 0;
    ArrayList<FoodCart> foodList = new ArrayList<>();

    GenerateBill() {
        // Set up the main frame
        setTitle("Generate Bill");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(255, 248, 220)); // Light cream background

        // Create input panel with nice styling
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2, 10, 10));
        inputPanel.setBackground(new Color(255, 248, 220)); // Same background color

        // Create input fields
        JLabel foodLabel = new JLabel("Food Name: ");
        foodLabel.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(foodLabel);
        food = new JTextField(20);
        inputPanel.add(food);

        JLabel quantityLabel = new JLabel("Quantity: ");
        quantityLabel.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(quantityLabel);
        quantity = new JTextField(20);
        inputPanel.add(quantity);

        // Button to add item
        JButton addButton = new JButton("Add Item");
        addButton.setBackground(new Color(34, 139, 34)); // Forest green
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Arial", Font.BOLD, 16));
        addButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        inputPanel.add(addButton);

        // Button to remove item
        JButton removeButton = new JButton("Remove Item");
        removeButton.setBackground(new Color(255, 69, 0)); // Red
        removeButton.setForeground(Color.WHITE);
        removeButton.setFont(new Font("Arial", Font.BOLD, 16));
        removeButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        inputPanel.add(removeButton);

        // Button to edit item
        JButton editButton = new JButton("Edit Item");
        editButton.setBackground(new Color(255, 215, 0)); // Gold
        editButton.setForeground(Color.WHITE);
        editButton.setFont(new Font("Arial", Font.BOLD, 16));
        editButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        inputPanel.add(editButton);

        // Create a table for the cart
        model = new DefaultTableModel(columnNames, 0) {
            // Make the table non-editable
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        cart = new JTable(model);
        cart.setFillsViewportHeight(true);
        cart.setBackground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(cart);

        // Create a panel for the total price and checkout button
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.setBackground(new Color(255, 248, 220)); // Same background color
        totalP.setFont(new Font("Arial", Font.BOLD, 16));
        bottomPanel.add(totalP, BorderLayout.WEST);

        JButton checkOutButton = new JButton("CheckOut");
        checkOutButton.setBackground(new Color(34, 139, 34)); // Forest green
        checkOutButton.setForeground(Color.WHITE);
        checkOutButton.setFont(new Font("Arial", Font.BOLD, 16));
        checkOutButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        bottomPanel.add(checkOutButton, BorderLayout.EAST);

        // Set up main frame layout
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Button actions
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addItemToCart();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeItemFromCart();
            }
        });

        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editItemInCart();
            }
        });

        checkOutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkOut();
            }
        });

        setVisible(true);
    }

    private void addItemToCart() {
        PreparedStatement pst;
        DBConnection con = new DBConnection();
        ResultSet rs;
        try {
            pst = con.mkDataBase().prepareStatement("SELECT f_prize, f_quantity FROM restaurantmanagement.food WHERE f_name = ?");
            pst.setString(1, food.getText());
            rs = pst.executeQuery();

            if (rs.next()) {
                FoodCart foodItem = new FoodCart();
                foodItem.name = food.getText();
                foodItem.quantity = Integer.parseInt(quantity.getText());
                double price = rs.getDouble("f_prize");
                foodItem.totalPer = foodItem.quantity * price;

                int currentQuantity = rs.getInt("f_quantity");
                if (currentQuantity >= foodItem.quantity) {
                    totalprice += foodItem.totalPer;
                    foodList.add(foodItem);

                    // Update the table model
                    model.addRow(new Object[]{foodItem.name, foodItem.quantity, foodItem.totalPer});

                    // Update total price display
                    totalP.setText("Total Price: " + totalprice + " tk");

                    // Clear input fields
                    food.setText("");
                    quantity.setText("");

                    // Display success message
                    showMessage("Item Added!", "Successfully added " + foodItem.name + " to the cart.", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    showMessage("Insufficient Quantity", "Only " + currentQuantity + " of " + foodItem.name + " available.", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                showMessage("Food Not Found", "The food item you entered was not found.", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            System.out.println(ex);
            showMessage("Error", "Error: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeItemFromCart() {
        int selectedRow = cart.getSelectedRow();
        if (selectedRow != -1) {
            String foodName = model.getValueAt(selectedRow, 0).toString();
            double price = (double) model.getValueAt(selectedRow, 2);
            int quantityRemoved = (int) model.getValueAt(selectedRow, 1);

            // Remove the item from the foodList
            foodList.removeIf(item -> item.name.equals(foodName) && item.quantity == quantityRemoved);

            // Update total price
            totalprice -= price;
            totalP.setText("Total Price: " + totalprice + " tk");

            // Remove the row from the table
            model.removeRow(selectedRow);

            showMessage("Item Removed!", "Successfully removed " + foodName + " from the cart.", JOptionPane.INFORMATION_MESSAGE);
        } else {
            showMessage("No Selection", "Please select an item to remove.", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void editItemInCart() {
        int selectedRow = cart.getSelectedRow();
        if (selectedRow != -1) {
            String foodName = model.getValueAt(selectedRow, 0).toString();
            double currentPrice = (double) model.getValueAt(selectedRow, 2);
            int currentQuantity = (int) model.getValueAt(selectedRow, 1);

            // Prompt the user for new quantity
            String newQuantityStr = JOptionPane.showInputDialog(this, "Edit quantity for " + foodName, currentQuantity);
            if (newQuantityStr != null) {
                try {
                    int newQuantity = Integer.parseInt(newQuantityStr);

                    // Calculate the new total price
                    double pricePerUnit = currentPrice / currentQuantity; // Get price per unit
                    double newTotalPrice = newQuantity * pricePerUnit;

                    // Update the total price
                    totalprice -= currentPrice; // Subtract old price
                    totalprice += newTotalPrice; // Add new price
                    totalP.setText("Total Price: " + totalprice + " tk");

                    // Update foodList and table
                    FoodCart foodItem = foodList.stream()
                            .filter(item -> item.name.equals(foodName))
                            .findFirst()
                            .orElse(null);
                    if (foodItem != null) {
                        foodItem.quantity = newQuantity;
                        foodItem.totalPer = newTotalPrice;

                        model.setValueAt(newQuantity, selectedRow, 1);
                        model.setValueAt(newTotalPrice, selectedRow, 2);
                    }

                    showMessage("Item Edited!", "Successfully updated quantity for " + foodName + ".", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException e) {
                    showMessage("Invalid Input", "Please enter a valid quantity.", JOptionPane.WARNING_MESSAGE);
                }
            }
        } else {
            showMessage("No Selection", "Please select an item to edit.", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void checkOut() {
        // Update the database for each food item
        for (FoodCart fc : foodList) {
            updateDatabase(fc.name, fc.quantity);
        }

        StringBuilder bill = new StringBuilder();
        double vat = 15;

        // Add a professional header for the bill
        bill.append("******************************************\n");
        bill.append("               EXOTIC CAFE\n");
        bill.append("          Thank you for dining with us!\n");
        bill.append("******************************************\n\n");

        // Add column headers for the bill
        String formatHeader = "%-20s %-10s %-15s\n";
        bill.append(String.format(formatHeader, "Food Name", "Quantity", "Price (tk)"));
        bill.append("-----------------------------------------------\n");

        // Add each item to the bill with aligned columns
        String formatItems = "%-20s %-10d %-15.2f\n";
        for (FoodCart fc : foodList) {
            bill.append(String.format(formatItems, fc.name, fc.quantity, fc.totalPer));
        }

        // Add total cost details with more clarity
        double totalCost = totalprice + (totalprice * vat / 100);
        bill.append("\n-----------------------------------------------\n");
        bill.append(String.format("%-30s %15.2f tk\n", "Subtotal:", totalprice));
        bill.append(String.format("%-30s %15.2f tk\n", "VAT (" + vat + "%):", (totalprice * vat / 100)));
        bill.append(String.format("%-30s %15.2f tk\n", "Total Cost:", totalCost));
        bill.append("-----------------------------------------------\n\n");

        // Additional information
        bill.append("For any queries, please call us at: 9701904466\n");
        bill.append("Thank you for dining with us!\n");
        bill.append("******************************************\n");

        // Show the bill in a JOptionPane
        showMessage("Checkout", bill.toString(), JOptionPane.INFORMATION_MESSAGE);

        this.dispose(); // Close the window after checkout
    }

    private void updateDatabase(String foodName, int quantity) {
        PreparedStatement pst;
        DBConnection con = new DBConnection();
        try {
            // Update the food quantity in the database
            pst = con.mkDataBase().prepareStatement("UPDATE restaurantmanagement.food SET f_quantity = f_quantity - ? WHERE f_name = ?");
            pst.setInt(1, quantity);
            pst.setString(2, foodName);
            pst.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
            showMessage("Error", "Error updating database: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showMessage(String title, String message, int messageType) {
        JOptionPane pane = new JOptionPane(message, messageType);
        JDialog dialog = pane.createDialog(title);
        dialog.setModal(true);
        dialog.setBackground(Color.white);
        dialog.setVisible(true);
    }

    class FoodCart {
        String name;
        Double totalPer;
        int quantity;
    }

    public static void main(String[] args) {
        new GenerateBill();
    }
}
