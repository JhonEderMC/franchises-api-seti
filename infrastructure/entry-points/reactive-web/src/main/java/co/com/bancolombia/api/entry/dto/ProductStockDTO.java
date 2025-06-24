package co.com.bancolombia.api.entry.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductStockDTO {
    private String productId;
    private String productName;
    private String branchId;
    private String branchName;
    private int stock;
}