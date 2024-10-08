package src;

import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ItemInfo {

    private JFrame mainFrame;
    private JLabel headerLabel;
    private JPanel controlPanel;
    private JTable table;
    private DefaultTableModel model;

    ItemInfo() {
        prepareGUI();
    }

    public static void main(String[] args) throws SQLException {
        ItemInfo swingControlDemo = new ItemInfo();
        swingControlDemo.showButtonDemo();
    }

    private void prepareGUI() {
        mainFrame = new JFrame("Showing All Items");
        mainFrame.setSize(800, 600);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set a light background color
        mainFrame.getContentPane().setBackground(new Color(240, 240, 240)); // Light gray background

        headerLabel = new JLabel("EXOTIC CAFE", JLabel.CENTER);
        headerLabel.setFont(new Font("Serif", Font.BOLD, 32));
        headerLabel.setForeground(new Color(34, 139, 34)); // Forest green
        mainFrame.add(headerLabel, BorderLayout.NORTH);

        controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout());
        controlPanel.setBackground(new Color(240, 240, 240)); // Same as main background
        mainFrame.add(controlPanel, BorderLayout.CENTER);

        // Table model
        model = new DefaultTableModel();
        model.addColumn("S.No"); // Adding serial number column
        model.addColumn("ID");
        model.addColumn("Food Name");
        model.addColumn("Price");
        model.addColumn("Quantity");

        // Initialize JTable with the model
        table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setShowGrid(false); // Remove grid lines for a cleaner look
        table.setBackground(Color.WHITE); // Set the background of the table
        table.setRowHeight(30); // Set the row height for better visibility

        // Adding a scroll pane to the table
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        controlPanel.add(scrollPane, BorderLayout.CENTER);

        mainFrame.setVisible(true);
        mainFrame.setLocationRelativeTo(null); // Center the frame
    }

    public void showButtonDemo() throws SQLException {
        String[] columnNames = {"ID", "Food Name", "Price", "Quantity"};
        Object[][] data = new Object[100][4];

        PreparedStatement pst;
        ResultSet rs;
        DBConnection con = new DBConnection();
        try {
            pst = con.mkDataBase().prepareStatement("SELECT * FROM restaurantmanagement.food");
            rs = pst.executeQuery();
            int i = 0;
            while (rs.next()) {
                data[i][0] = rs.getInt("f_id"); // Actual ID from the database
                data[i][1] = rs.getString("f_name");
                data[i][2] = rs.getDouble("f_prize");
                data[i][3] = rs.getInt("f_quantity");
                i++;
            }
            mainFrame.setVisible(false);

        } catch (Exception ex) {
            System.out.println(ex);
            JOptionPane.showMessageDialog(null, "Error retrieving data!");
        }

        // Adding data to the table with serial numbers starting from 1
        for (int j = 0; j < data.length; j++) {
            if (data[j][0] != null) {
                // Add a new row with serial number (j + 1) as the first column
                model.addRow(new Object[]{j + 1, data[j][0], data[j][1], data[j][2], data[j][3]});
            }
        }

        mainFrame.setVisible(true);
    }

    // Method to handle billing an item
    public void billItem(int id, int quantityToBill) {
        // Get the current quantity from the table
        int currentQuantity = (int) model.getValueAt(id, 3); // Assuming 'id' is the row index in the table

        if (currentQuantity >= quantityToBill) {
            // Update the database and table
            int newQuantity = currentQuantity - quantityToBill;
            updateDatabase(id, newQuantity);
            model.setValueAt(newQuantity, id, 3); // Update the quantity in the table
            JOptionPane.showMessageDialog(mainFrame, "Item billed successfully!");
        } else {
            JOptionPane.showMessageDialog(mainFrame, "Not enough quantity available!");
        }
    }

    private void updateDatabase(int id, int quantity) {
        PreparedStatement pst;
        DBConnection con = new DBConnection();
        try {
            pst = con.mkDataBase().prepareStatement("UPDATE restaurantmanagement.food SET f_quantity=? WHERE f_id=?");
            pst.setInt(1, quantity);
            pst.setInt(2, id);
            pst.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
            JOptionPane.showMessageDialog(mainFrame, "Error updating database: " + ex.getMessage());
        }
    }
}
