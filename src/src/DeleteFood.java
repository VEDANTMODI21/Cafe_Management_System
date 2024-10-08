package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteFood {

    private JFrame mainFrame;
    private JLabel headerLabel;
    private JPanel controlPanel;
    private JLabel nameLabel;
    private JTextField nameField;
    private JButton deleteButton;

    DeleteFood() {
        prepareGUI();
    }

    public static void main(String[] args) {
        DeleteFood deleteFoodDemo = new DeleteFood();
        deleteFoodDemo.showButtonDemo();
    }

    private void prepareGUI() {
        mainFrame = new JFrame("Delete Food Item");
        mainFrame.setSize(700, 400);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a custom panel with a background image
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("C://Users//HP//IdeaProjects//90_94_canteen//src//image//Delete.jpg");
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());

        mainFrame.add(backgroundPanel);

        headerLabel = new JLabel("EXOTIC CAFE", JLabel.CENTER);
        headerLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 34));
        headerLabel.setForeground(new Color(255, 215, 0)); // Gold color for the header

        controlPanel = new JPanel();
        controlPanel.setBackground(new Color(255, 255, 255, 220)); // Semi-transparent white background
        controlPanel.setBorder(BorderFactory.createTitledBorder("Delete Food Item"));
        controlPanel.setLayout(new GridBagLayout());

        // Set a colorful border for the control panel
        controlPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.CYAN, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding

        // Add components to the background panel
        gbc.gridx = 0; // Position of headerLabel
        gbc.gridy = 0; // Position of headerLabel
        backgroundPanel.add(headerLabel, gbc);

        gbc.gridx = 0; // Reset x position for control panel
        gbc.gridy = 1; // Position below headerLabel
        backgroundPanel.add(controlPanel, gbc);

        mainFrame.setVisible(true);
    }

    public void showButtonDemo() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nameLabel = new JLabel("Enter Food Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        nameLabel.setForeground(new Color(0, 102, 204)); // Dark blue color
        controlPanel.add(nameLabel, gbc);

        nameField = new JTextField(15);
        nameField.setFont(new Font("Arial", Font.PLAIN, 16));
        controlPanel.add(nameField, gbc);

        deleteButton = new JButton("DELETE");
        deleteButton.setFont(new Font("Arial", Font.BOLD, 16));
        deleteButton.setBackground(new Color(255, 69, 0)); // Red-Orange color
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.setPreferredSize(new Dimension(200, 40)); // Increase the length of the button
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PreparedStatement pst;
                DBConnection con = new DBConnection();
                try {
                    pst = con.mkDataBase().prepareStatement("DELETE FROM restaurantmanagement.food WHERE f_name = ?");
                    pst.setString(1, nameField.getText());

                    int rowsAffected = pst.executeUpdate(); // Use executeUpdate for DELETE

                    if (rowsAffected > 0) {
                        showCustomMessageDialog("Success", "Item Deleted: " + nameField.getText(), JOptionPane.INFORMATION_MESSAGE);
                        nameField.setText(""); // Clear input field
                    } else {
                        showCustomMessageDialog("Error", "Item not found: " + nameField.getText(), JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    showCustomMessageDialog("Error", "Database error occurred: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showCustomMessageDialog("Error", "An unexpected error occurred.", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 2; // Next row
        gbc.gridwidth = 2; // Span across two columns
        controlPanel.add(deleteButton, gbc);

        mainFrame.setVisible(true);
        mainFrame.setLocationRelativeTo(null);
    }

    private void showCustomMessageDialog(String title, String message, int messageType) {
        // Create a custom JOptionPane
        JOptionPane optionPane = new JOptionPane(message, messageType, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
        JDialog dialog = optionPane.createDialog(title);

        // Customize the dialog appearance
        dialog.setSize(350, 200); // Set the size of the dialog
        dialog.setBackground(new Color(255, 255, 255)); // White background

        // Create a panel for the message
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BorderLayout());
        messagePanel.setBackground(new Color(255, 255, 255)); // White background

        // Set a custom font for the message
        JLabel label = new JLabel(message);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setForeground(new Color(0, 102, 204)); // Custom font color
        label.setHorizontalAlignment(JLabel.CENTER);
        messagePanel.add(label, BorderLayout.CENTER);

        // Add a custom icon
        ImageIcon icon = new ImageIcon("C://path_to_your_icon/icon.png"); // Change the path to your icon
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setHorizontalAlignment(JLabel.CENTER);
        messagePanel.add(iconLabel, BorderLayout.NORTH);

        // Add button to close dialog
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> dialog.dispose());
        messagePanel.add(okButton, BorderLayout.SOUTH);

        optionPane.setMessage(messagePanel);

        dialog.setVisible(true);
    }
}
