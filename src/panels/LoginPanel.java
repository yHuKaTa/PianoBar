package panels;

import frames.BarFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LoginPanel extends BasePanel{
    private final JPasswordField pinTextField = new JPasswordField();
    public LoginPanel(BarFrame frame) {
        super(frame);
        JLabel barLabel = new JLabel("Пиян-оО Бар");
        barLabel.setBounds((frame.getWidth()/2)-100,10,200,30);
        barLabel.setFont(new Font("Helvetica",Font.BOLD,25));
        barLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(barLabel);

        Image img = null;
        try {
            Path path = Paths.get("resources/logo.png");
            img = ImageIO.read(new BufferedInputStream(new FileInputStream(path.toAbsolutePath().toString())));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Логото не може да се прочете!");
        }
        JLabel logo = new JLabel(new ImageIcon(img));
        logo.setBounds((frame.getWidth()/2)-80,barLabel.getY()+ 50,150,200);
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        add(logo);

        JLabel welcomeLabel = new JLabel("Вписване");
        welcomeLabel.setBounds((frame.getWidth()/2)-100,logo.getY()+220,200,30);
        welcomeLabel.setFont(new Font("Helvetica",Font.BOLD,20));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(welcomeLabel);

        JLabel enterPassLabel = new JLabel("Въведи парола");
        enterPassLabel.setFont(new Font("Arial",Font.BOLD,15));
        enterPassLabel.setBounds((frame.getWidth())/2-60, welcomeLabel.getY() + 60, 120, 25);
        enterPassLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(enterPassLabel);

        pinTextField.setFont(new Font("Arial",Font.BOLD,10));
        pinTextField.setHorizontalAlignment(SwingConstants.CENTER);
        pinTextField.setBounds((frame.getWidth())/2-60, enterPassLabel.getY() + 50, 120,20);
        add(pinTextField);

        JButton loginButton = new JButton("ВХОД");
        loginButton.setFont(new Font("Arial",Font.BOLD,12));
        loginButton.setHorizontalAlignment(SwingConstants.CENTER);
        loginButton.setBounds((frame.getWidth())/2-50, pinTextField.getY() + 50, 100, 30);
        loginButton.addActionListener(e -> help.loginSelect(String.valueOf(pinTextField.getPassword()), frame));
        add(loginButton);

        JButton exitButton = new JButton("ИЗХОД");
        exitButton.setFont(new Font("Arial",Font.BOLD,12));
        exitButton.setHorizontalAlignment(SwingConstants.CENTER);
        exitButton.setBounds((frame.getWidth())/2-50, loginButton.getY() + 60, 100, 30);
        exitButton.addActionListener(e -> System.exit(0));
        add(exitButton);
    }
}
