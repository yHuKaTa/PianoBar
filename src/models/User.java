package models;

public class User {
    private String name;
    private String pinCode;
    private String phoneNumber;
    private UserType type;

    public User(){

    }

    public User(String name, String pinCode, String phoneNumber, UserType type) {
        this.name = name;
        this.pinCode = pinCode;
        this.phoneNumber = phoneNumber;
        this.type = type;
    }

    public String getUserName() {
        return name;
    }

    public void setUserName(String userName){this.name = userName;}

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
            this.type = type;
    }
}

