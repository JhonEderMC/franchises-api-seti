package co.com.bancolombia.model;

import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder(toBuilder = true)
@With
public class Franchise {
    private String id;
    private String name;
    @Builder.Default
    private List<Branch> branches = new ArrayList<>();

    public static Franchise create(String name) {
        return Franchise.builder()
                .name(name)
                .branches(new ArrayList<>())
                .build();
    }

    public Franchise addBranch(Branch branch) {
        List<Branch> updatedBranches = new ArrayList<>(this.branches);
        updatedBranches.add(branch);
        return this.toBuilder().branches(updatedBranches).build();
    }
}