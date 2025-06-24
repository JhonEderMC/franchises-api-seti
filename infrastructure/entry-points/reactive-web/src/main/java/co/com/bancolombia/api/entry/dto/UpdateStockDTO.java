package co.com.bancolombia.api.entry.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStockDTO {
    @Min(value = 0, message = "Stock must be non-negative")
    private int stock;
}
