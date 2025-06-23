package co.com.bancolombia.api.entry.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FranchiseDTO {
    private String id;

    @NotBlank(message = "Franchise name is required")
    private String name;
}
