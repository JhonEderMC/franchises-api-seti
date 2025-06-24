package co.com.bancolombia.model;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class FranchiseTest {

    static Branch branch(String id, String name) {
        return Branch.builder().id(id).name(name).build();
    }

    @Test
    void shouldBuildFranchiseWithBuilder() {
        Franchise franchise = Franchise.builder()
                .id("1")
                .name("Franchise A")
                .branches(List.of())
                .build();

        assertEquals("1", franchise.getId());
        assertEquals("Franchise A", franchise.getName());
        assertNotNull(franchise.getBranches());
        assertTrue(franchise.getBranches().isEmpty());
    }

    @Test
    void shouldCreateFranchiseWithStaticMethod() {
        Franchise franchise = Franchise.create("Franchise B");
        assertEquals("Franchise B", franchise.getName());
        assertNotNull(franchise.getBranches());
        assertTrue(franchise.getBranches().isEmpty());
    }

    @Test
    void shouldAddBranch() {
        Franchise franchise = Franchise.create("Franchise C");
        Branch b = branch("B1", "Branch 1");

        Franchise updated = franchise.addBranch(b);

        assertEquals(0, franchise.getBranches().size());
        assertEquals(1, updated.getBranches().size());
        assertEquals("B1", updated.getBranches().get(0).getId());
    }
}