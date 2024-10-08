package src;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class Frame2new {

    private JFrame mainFrame;
    private JLabel headerLabel;
    private JLabel statusLabel;
    private JPanel controlPanel;
    private JLabel backgroundLabel;

    public Frame2new() {
        prepareGUI();
    }

    public static void main(String[] args) {
        Frame2new swingControlDemo = new Frame2new();
        swingControlDemo.showButtonDemo();
    }

    private void prepareGUI() {
        mainFrame = new JFrame("Cafe Management System");
        mainFrame.setBounds(100, 100, 700, 400);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the layout to null for custom positioning
        mainFrame.setLayout(null);

        // Load the background image
        ImageIcon backgroundImage = new ImageIcon("/C://Users//HP//IdeaProjects//90_94_canteen//src//image//COFEE FOR CMS (2).jpg/");

        // Create a JLabel with the background image
        backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setBounds(0, 0, 700, 400); // Adjust to frame size
        mainFrame.setContentPane(backgroundLabel);

        // Create a transparent panel to overlay components
        JPanel transparentPanel = new JPanel();
        transparentPanel.setBounds(0, 0, 700, 400);
        transparentPanel.setOpaque(false); // Make the panel transparent
        transparentPanel.setLayout(new GridLayout(3, 1));

        mainFrame.getContentPane().add(transparentPanel);

        headerLabel = new JLabel("", JLabel.CENTER);
        statusLabel = new JLabel("", JLabel.CENTER);
        statusLabel.setSize(350, 300);

        controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(1, 5));
        controlPanel.setOpaque(false); // Make control panel transparent

        transparentPanel.add(headerLabel);
        transparentPanel.add(controlPanel);
        transparentPanel.add(statusLabel);

        // Enhance the heading of "EXOTIC CAFE"
        headerLabel.setText("EXOTIC CAFE");
        headerLabel.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 40)); // Larger, stylish font
        headerLabel.setForeground(Color.white); // Text color

        // Adding gradient effect to the text using HTML for JLabel
        headerLabel.setText("<html><div style='text-align: center;'>"
                + "<span style='font-size: 40px; font-family: Serif; color: linear-gradient(to right, #FF5733, #FFC300);"
                + " text-shadow: 2px 2px 5px rgba(0,0,0,0.7);'>EXOTIC CAFE</span></div></html>");

        // Add border around the label
        headerLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE));

        mainFrame.setVisible(true);
        mainFrame.setLocationRelativeTo(null);
    }

    public void showButtonDemo() {
        // Create beautified buttons with enhanced colors
        JButton fkButton = createStyledButton("Food Info");
        JButton billButton = createStyledButton("Billing Info");
        JButton afButton = createStyledButton("Insert Item");
        JButton ufButton = createStyledButton("Update Item");
        JButton dlButton = createStyledButton("Delete Item");

        // Add action listeners for buttons
        fkButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ItemInfo ii = new ItemInfo();
                try {
                    ii.showButtonDemo();
                } catch (SQLException ex) {
                    Logger.getLogger(Frame2new.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        billButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GenerateBill gb = new GenerateBill();
            }
        });

        afButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                EnterFood ef = new EnterFood();
                ef.showButtonDemo();
            }
        });

        ufButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                UpdateFood uf = new UpdateFood();
                uf.showButtonDemo();
            }
        });

        dlButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DeleteFood dl = new DeleteFood();
                dl.showButtonDemo();
            }
        });

        // Add buttons to control panel
        controlPanel.add(ufButton);
        controlPanel.add(afButton);
        controlPanel.add(billButton);
        controlPanel.add(fkButton);
        controlPanel.add(dlButton);

        mainFrame.setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);

        // Set button font
        button.setFont(new Font("Arial", Font.BOLD, 14));

        // Set background gradient-like color
        button.setBackground(new Color(70, 130, 180));     // Steel blue
        button.setForeground(Color.WHITE);                 // Set text color to white

        // Set button padding
        button.setMargin(new Insets(10, 20, 10, 20));

        // Add a raised border with color
        button.setBorder(BorderFactory.createLineBorder(new Color(30, 144, 255), 3));

        // Set button rounded corners using custom UI
        button.setFocusPainted(false);                     // Remove focus painting
        button.setContentAreaFilled(false);                // Transparent background
        button.setOpaque(true);                            // Make button background visible

        // Mouse hover effect (change background on hover)
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(new Color(65, 105, 225));  // Royal blue on hover
            }

            public void mouseExited(MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180)); // Reset to original color
            }
        });

        return button;
    }
}
