package co.com.bancolombia.model;

import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder(toBuilder = true)
@With
public class Branch {
    private String id;
    private String name;
    private String franchiseId;
    @Builder.Default
    private List<Product> products = new ArrayList<>();

    public static Branch create(String name, String franchiseId) {
        return Branch.builder()
                .name(name)
                .franchiseId(franchiseId)
                .products(new ArrayList<>())
                .build();
    }

    public Branch addProduct(Product product) {
        List<Product> updatedProducts = new ArrayList<>(this.products);
        updatedProducts.add(product);
        return this.toBuilder().products(updatedProducts).build();
    }

    public Branch removeProduct(String productId) {
        List<Product> updatedProducts = new ArrayList<>(this.products);
        updatedProducts.removeIf(p -> p.getId().equals(productId));
        return this.toBuilder().products(updatedProducts).build();
    }

    public Branch updateProductStock(String productId, int newStock) {
        List<Product> updatedProducts = new ArrayList<>();
        for (Product product : this.products) {
            if (product.getId().equals(productId)) {
                updatedProducts.add(product.withStock(newStock));
            } else {
                updatedProducts.add(product);
            }
        }
        return this.toBuilder().products(updatedProducts).build();
    }
}