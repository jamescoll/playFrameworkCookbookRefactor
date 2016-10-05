package models;



import play.data.validation.Constraints;
import play.mvc.PathBindable;


public class Product implements PathBindable<Product>, java.io.Serializable {

    @Constraints.Required
    private String sku;

    @Constraints.Required
    private String title;

    private Double price;



    public Product(){

    }

    public Product(String sku, String title){
        this.sku = sku;
        this.title = title;
    }

    public Product(String sku, String title, Double price){
        this.sku = sku;
        this.title = title;
        this.price = price;
    }

    @Deprecated
    private static final java.util.Map<String, String> productMap = new java.util.HashMap<String, String>();
    static {
        productMap.put("ABC", "8-Port Switch");
        productMap.put("DEF", "16-Port Switch");
        productMap.put("GHI", "24-Port Switch");
    }

    @Deprecated
    public static void add(Product product) {
        productMap.put(product.sku, product.title);
    }

    @Deprecated
    public static java.util.List<Product> getProducts() {
        java.util.List<Product> productList = new java.util.ArrayList<Product>();
        for (java.util.Map.Entry<String, String> entry : productMap.entrySet()) {
            Product p = new Product();
            p.sku = entry.getKey();
            p.title = entry.getValue();
            productList.add(p);
        }
        return productList;
    }

    @Deprecated
    public Product bind(String key, String value) {
        String product = productMap.get(value);
        if (product != null) {
            Product p = new Product();
            p.sku = value;
            p.title = product;

            return p;
        } else {
            throw new IllegalArgumentException("Product with sku " + value + " not found");
        }
    }

    @Deprecated
    public String unbind(String key) {
        return sku;
    }

    @Deprecated
    public String javascriptUnbind() {
        return "function(k,v) {\n" +
                "    return v.sku;" +
                "}";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Double getPrice() { return price; }

    public void setPrice(Double price) { this.price = price; }


}