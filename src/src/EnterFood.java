package src;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EnterFood {

    private JFrame mainFrame;
    private JLabel headerLabel;
    private JPanel controlPanel;
    private JLabel nameLabel, priceLabel, quantityLabel;
    private JTextField nameField, priceField, quantityField;
    private JButton okButton;

    EnterFood() {
        prepareGUI();
    }

    public static void main(String[] args) {
        EnterFood swingControlDemo = new EnterFood();
        swingControlDemo.showButtonDemo();
    }

    private void prepareGUI() {
        mainFrame = new JFrame("Insert a New Food Item!");
        mainFrame.setSize(700, 400);
        mainFrame.setLayout(new GridBagLayout());
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set a background image
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("C:\\Users\\HP\\IdeaProjects\\90_94_canteen\\src\\image\\Enter.jpg");
                Image img = background.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
                g.drawImage(img, 0, 0, this);
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());
        mainFrame.add(backgroundPanel);

        headerLabel = new JLabel("EXOTIC CAFE", JLabel.CENTER);
        headerLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 34));
        headerLabel.setForeground(new Color(255, 215, 0)); // Gold color for the header

        controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(4, 2, 10, 10)); // 4 rows, 2 columns with gaps
        controlPanel.setBackground(new Color(255, 255, 255, 220)); // Semi-transparent white background
        controlPanel.setBorder(BorderFactory.createTitledBorder("Add New Food Item"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding
        gbc.gridx = 0;
        gbc.gridy = 0;
        backgroundPanel.add(headerLabel, gbc);

        gbc.gridy = 1;
        backgroundPanel.add(controlPanel, gbc);

        mainFrame.setVisible(true);
    }

    public void showButtonDemo() {
        nameLabel = new JLabel("Enter Name:");
        priceLabel = new JLabel("Enter Price:");
        quantityLabel = new JLabel("Enter Quantity:");

        nameField = new JTextField();
        priceField = new JTextField();
        quantityField = new JTextField();

        // Button design
        okButton = new JButton("OK");
        okButton.setFont(new Font("Arial", Font.BOLD, 16));
        okButton.setBackground(new Color(0, 204, 102)); // Green color
        okButton.setForeground(Color.WHITE);
        okButton.setFocusPainted(false);
        okButton.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2)); // Add a border
        okButton.setPreferredSize(new Dimension(150, 40)); // Set button size
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PreparedStatement pst;
                DBConnection con = new DBConnection();
                try {
                    pst = con.mkDataBase().prepareStatement("INSERT INTO restaurantmanagement.food(f_name, f_prize, f_quantity) VALUES (?, ?, ?)");
                    pst.setString(1, nameField.getText());
                    pst.setDouble(2, Double.parseDouble(priceField.getText()));
                    pst.setInt(3, Integer.parseInt(quantityField.getText()));
                    pst.execute();

                    JOptionPane.showMessageDialog(mainFrame, "Done Inserting " + nameField.getText(), "Success", JOptionPane.INFORMATION_MESSAGE);
                    mainFrame.setVisible(false);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(mainFrame, "Inserting Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(mainFrame, "Inserting Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Add components to control panel
        controlPanel.add(nameLabel);
        controlPanel.add(nameField);
        controlPanel.add(priceLabel);
        controlPanel.add(priceField);
        controlPanel.add(quantityLabel);
        controlPanel.add(quantityField);
        controlPanel.add(okButton);

        // Center the frame on the screen
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }
}
