package co.com.bancolombia.model;

import lombok.Builder;
import lombok.Data;
import lombok.With;

@Data
@Builder(toBuilder = true)
@With
public class Product {
    private String id;
    private String name;
    private int stock;
    private String branchId;

    public static Product create(String name, int stock, String branchId) {
        return Product.builder()
                .name(name)
                .stock(stock)
                .branchId(branchId)
                .build();
    }
}
