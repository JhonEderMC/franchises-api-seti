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
public class BranchDTO {
    private String id;

    @NotBlank(message = "Branch name is required")
    private String name;

    private String franchiseId;
}
