package co.com.bancolombia.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductStock {
    private String productId;
    private String productName;
    private String branchId;
    private String branchName;
    private int stock;
}
