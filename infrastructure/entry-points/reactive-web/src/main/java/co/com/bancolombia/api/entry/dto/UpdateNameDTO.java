package co.com.bancolombia.api.entry.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNameDTO {
    @NotBlank(message = "Name is required")
    private String name;
}
