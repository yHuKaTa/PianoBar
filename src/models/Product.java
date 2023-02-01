package models;

public class Product {
    private ProductType type;
    private String subtype;
    private String brandName;
    private final double servedQuantity;
    private double quantity;
    private double price;
    private boolean isLiquid;
    private boolean canDecrease;

    public Product(ProductType type, String subtype, String brandName,  double servedQuantity, double quantity, double price, boolean isLiquid) {
        this.type = type;
        this.subtype = subtype;
        this.brandName = brandName;
        this.servedQuantity = servedQuantity;
        this.quantity = quantity;
        this.price = price;
        this.isLiquid = isLiquid;
    }
    public ProductType getType(){return this.type;}

    public void setType(ProductType type) {
        this.type = type;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }


    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }


    public double getServedQuantity() {
        return this.servedQuantity;
    }


    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getQuantity() {
            return this.quantity;
    }

    public void decreaseQuantity(){
        this.quantity = (quantity - servedQuantity);
    }

    public void increaseQuantity() {
        this.quantity = (quantity + servedQuantity);
    }
    public void addQuantity(double quantity) {
        this.quantity = this.quantity + quantity;
    }

    public double getPrice() { // Because servedQuantity
        if (this.type == ProductType.ALCOHOLIC){
            return this.quantity * this.price * 20;
        } else if (this.type == ProductType.FOOD || this.type == ProductType.COCKTAIL) {
            return this.quantity * this.price * 5;
        } else if (this.type == ProductType.NONALCOHOLIC) {
            return this.quantity * this.price * 4;
        } else {
            return this.quantity * this.price * 5;
        }
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getMeasure() {
        if (isLiquid){
            return "мл.";
        } else {
            return "гр.";
        }
    }
}
