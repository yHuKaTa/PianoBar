package database;

import models.Product;
import models.User;
import models.UserType;

import javax.swing.*;
import java.util.*;

public class Verification {
    User loggedUser(String pin, List<User> users) {
        User loggedUser = new User();
        for (User user : users) {
            if (user.getPinCode().equals(pin))
                loggedUser = user;
        }
        return loggedUser;
    }

    public boolean isTableOpened(int tableNumber, LinkedHashMap<Integer,User> workingUsers, User loggedUser, Map<Integer,ArrayList<Product>> orders) {
        boolean isOpened = false;
        if (Objects.isNull(workingUsers.get(tableNumber))) {
            isOpened = true;
        } else if (workingUsers.get(tableNumber).getUserName().equals(loggedUser.getUserName())) {
            isOpened = true;
        } else if (loggedUser.getType() == UserType.MANAGER || loggedUser.getType() == UserType.OWNER) {
            isOpened = true;
        } else if (orders.get(tableNumber).size() == 0) {
            isOpened = true;
        }
        return isOpened;
    }

    public int isValidNewTableNumber(){
        int newTableNumber;
        try {
            newTableNumber = Integer.parseInt(JOptionPane.showInputDialog("Въведи на коя маса искате да преместите поръчката:"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Въведи правилна маса!", "Грешна маса", JOptionPane.ERROR_MESSAGE);
            newTableNumber = Integer.parseInt(JOptionPane.showInputDialog("Въведи на коя маса искате да преместите поръчката:"));
        }
        while (newTableNumber <= 0 || newTableNumber > 20){
            try {
                JOptionPane.showMessageDialog(null, "Въведи правилна маса!", "Грешна маса", JOptionPane.ERROR_MESSAGE);
                newTableNumber = Integer.parseInt(JOptionPane.showInputDialog("Въведи на коя маса искате да преместите поръчката:"));
            } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Въведи правилна маса!", "Грешна маса", JOptionPane.ERROR_MESSAGE);
                    newTableNumber = Integer.parseInt(JOptionPane.showInputDialog("Въведи на коя маса искате да преместите поръчката:"));
            }
        }
        return newTableNumber;
    }


    boolean isPinDubt(String pin, ArrayList<User> users) {
        boolean isDubt = false;
        for (User user : users) {
            if (user.getPinCode().equalsIgnoreCase(pin)) {
                isDubt = true;
                break;
            }
        }
        return isDubt;
    }
    boolean isNameDubt(String fullName, ArrayList<User> users){
        boolean isDubt = false;
        for (User user : users) {
            if (user.getUserName().equalsIgnoreCase(fullName)) {
                isDubt = true;
                break;
            }
        }
        return isDubt;
    }
    boolean isPhoneNumberDubt(String phoneNumber, ArrayList<User> users) {
        boolean isDubt = false;
        for (User user : users) {
            if (user.getPhoneNumber().equalsIgnoreCase(phoneNumber)) {
                isDubt = true;
                break;
            }
        }
        return isDubt;
    }

}
