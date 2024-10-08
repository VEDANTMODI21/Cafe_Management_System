package src;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UpdateFood {

    private JFrame mainFrame;
    private JLabel headerLabel;
    private JPanel controlPanel;
    private JLabel nameLabel, priceLabel, quantityLabel;
    private JTextField nameField, priceField, quantityField;
    private JButton okButton;

    UpdateFood() {
        prepareGUI();
    }

    public static void main(String[] args) {
        UpdateFood swingControlDemo = new UpdateFood();
        swingControlDemo.showButtonDemo();
    }

    private void prepareGUI() {
        mainFrame = new JFrame("Update Food Item");
        mainFrame.setSize(700, 400);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set a background image
        ImageIcon backgroundImage = new ImageIcon("C://Users//HP//IdeaProjects//90_94_canteen//src//image//Coffee (1).jpg");
        JLabel background = new JLabel(backgroundImage);
        mainFrame.setContentPane(background);
        mainFrame.setLayout(new BorderLayout());

        // Header Label
        headerLabel = new JLabel("EXOTIC CAFE", JLabel.CENTER);
        headerLabel.setFont(new Font("Serif", Font.BOLD, 32));
        headerLabel.setForeground(new Color(34, 139, 34)); // Forest green
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // Add some space
        mainFrame.add(headerLabel, BorderLayout.NORTH);

        // Control Panel
        controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(4, 2, 10, 10)); // 4 rows, 2 columns
        controlPanel.setOpaque(false); // Make the panel transparent

        nameLabel = new JLabel("Enter Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        nameLabel.setForeground(Color.WHITE);
        controlPanel.add(nameLabel);

        nameField = new JTextField();
        nameField.setFont(new Font("Arial", Font.PLAIN, 20));
        controlPanel.add(nameField);

        priceLabel = new JLabel("Enter Price:");
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        priceLabel.setForeground(Color.WHITE);
        controlPanel.add(priceLabel);

        priceField = new JTextField();
        priceField.setFont(new Font("Arial", Font.PLAIN, 20));
        controlPanel.add(priceField);

        quantityLabel = new JLabel("Enter Quantity:");
        quantityLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        quantityLabel.setForeground(Color.WHITE);
        controlPanel.add(quantityLabel);

        quantityField = new JTextField();
        quantityField.setFont(new Font("Arial", Font.PLAIN, 20));
        controlPanel.add(quantityField);

        okButton = new JButton("UPDATE");
        okButton.setFont(new Font("Arial", Font.BOLD, 22));
        okButton.setBackground(new Color(34, 139, 34)); // Forest green
        okButton.setForeground(Color.WHITE);
        okButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        okButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateFoodItem();
            }
        });
        controlPanel.add(okButton);

        mainFrame.add(controlPanel, BorderLayout.CENTER);

        mainFrame.setVisible(true);
    }

    public void showButtonDemo() {
        mainFrame.setLocationRelativeTo(null); // Center the frame on screen
    }

    private void updateFoodItem() {
        PreparedStatement pst;
        DBConnection con = new DBConnection();
        try {
            pst = con.mkDataBase().prepareStatement("UPDATE restaurantmanagement.food SET f_quantity= ?, f_prize=? WHERE f_name = ?");
            pst.setInt(1, Integer.parseInt(quantityField.getText()));
            pst.setDouble(2, Double.parseDouble(priceField.getText()));
            pst.setString(3, nameField.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(mainFrame, "Successfully Updated " + nameField.getText(),
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            mainFrame.setVisible(false);
        } catch (SQLException ex) {
            Logger.getLogger(UpdateFood.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(mainFrame, "Error Updating Item: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(mainFrame, "Please enter valid numeric values for Price and Quantity.",
                    "Input Error", JOptionPane.WARNING_MESSAGE);
        }
    }
}
