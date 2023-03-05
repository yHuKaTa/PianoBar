package panels;

import frames.BarFrame;
import models.Product;
import models.User;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Map;

public class NewProductPanel extends BasePanel{
    public NewProductPanel(BarFrame frame, User loggedUser, Map<Integer, ArrayList<Product>> orders) {
        super(frame);

        JButton backButton = new JButton("Назад");
        backButton.setBounds(30, frame.getHeight() - 80, 120, 30);
        backButton.setHorizontalAlignment(SwingConstants.CENTER);
        backButton.addActionListener(e -> frame.router.showDeliveryPanel(loggedUser,orders));
        add(backButton);
    }

}
