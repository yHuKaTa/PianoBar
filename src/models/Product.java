package models;

public class Product {
    private ProductType type;
    private String subtype;
    private String brandName;
    private final double servedQuantity;
    private double quantity;
    private double price;
    private final boolean isLiquid;
    private boolean canDecrease; // for decrease principal

    private double lastOrderedQuantity;
    private double lastConsumedQuantity;
    private int discount;

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

    public void setType(int type) {
        if (type == 0) {
            this.type = ProductType.ALCOHOLIC;
        } else if (type == 1) {
            this.type = ProductType.NONALCOHOLIC;
        } else if (type == 2) {
            this.type = ProductType.FOOD;
        } else if (type == 3) {
            this.type = ProductType.COCKTAIL;
        }
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
        return Math.round((this.servedQuantity)*100.0)/100.0;
    }


    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getQuantity() {
            return Math.round((this.quantity)*100.0)/100.0;
    }

    public void decreaseQuantity(){
        this.quantity = Math.round((quantity - servedQuantity)*100.0)/100.0;
    }

    public void increaseQuantity() {
        this.quantity = Math.round((quantity + servedQuantity)*100.0)/100.0;
    }
    public void addQuantity(double quantity) {
        this.quantity = this.quantity + quantity;
    }

    public double getPrice() {
        return this.price;
    }
    public double getTotalPrice() { // Because of servedQuantity
        int resulted = 100 - this.discount;
        if (this.type == ProductType.ALCOHOLIC){
            return (this.quantity * this.price * resulted * 20)/100;
        } else if (this.type == ProductType.FOOD || this.type == ProductType.COCKTAIL) {
            return (this.quantity * this.price * resulted * 5)/100;
        } else if (this.type == ProductType.NONALCOHOLIC) {
            return (this.quantity * this.price * resulted * 4)/100;
        } else {
            return (this.quantity * this.price * resulted * 5)/100;
        }
    }

    public void setDiscount(int discount) {
        this.discount = discount;
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

    public boolean isCanDecrease() {
        return canDecrease;
    }

    public void setCanDecrease(boolean canDecrease) {
        this.canDecrease = canDecrease;
    }

    public double getLastOrderedQuantity() {
        return lastOrderedQuantity;
    }

    public void setLastOrderedQuantity(double lastOrderedQuantity) {
        this.lastOrderedQuantity = lastOrderedQuantity;
    }

    public double getLastConsumedQuantity() {
        return lastConsumedQuantity;
    }
    public void setLastConsumedQuantity(double lastConsumedQuantity) {
        this.lastConsumedQuantity = lastConsumedQuantity;
    }
    public Product clone(Product product) {
        return new Product(product.type,product.subtype,product.brandName,
                product.servedQuantity,product.quantity,product.price,product.isLiquid);
    }
}
