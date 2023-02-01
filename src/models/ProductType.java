package models;

public enum ProductType {
    ALCOHOLIC("Алкохоли"),
    NONALCOHOLIC("Безалкохолни"),
    FOOD("Храни"),
    COCKTAIL("Коктейли");
    public final String label;
    ProductType(String label){
        this.label = label;
    }
}
