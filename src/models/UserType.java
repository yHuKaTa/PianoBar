package models;

public enum UserType {
    WAITRESS("Сервитьор"),
    MANAGER("Управител"),
    OWNER("Собственик");

    public final String label;
    UserType(String label){
        this.label = label;
    }
}
