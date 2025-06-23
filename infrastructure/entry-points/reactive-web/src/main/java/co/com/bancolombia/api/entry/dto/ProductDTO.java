package co.com.bancolombia.api.entry.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private String id;

    @NotBlank(message = "Product name is required")
    private String name;

    @Min(value = 0, message = "Stock must be non-negative")
    private int stock;

    private String branchId;
}
