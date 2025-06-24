package co.com.bancolombia.model;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class BranchTest {

    static Product product(String id, int stock) {
        return Product.builder().id(id).stock(stock).build();
    }

    @Test
    void shouldBuildBranchWithBuilder() {
        Branch branch = Branch.builder()
                .id("1")
                .name("Branch A")
                .franchiseId("F1")
                .build();

        assertEquals("1", branch.getId());
        assertEquals("Branch A", branch.getName());
        assertEquals("F1", branch.getFranchiseId());
        assertNotNull(branch.getProducts());
        assertTrue(branch.getProducts().isEmpty());
    }

    @Test
    void shouldCreateBranchWithStaticMethod() {
        Branch branch = Branch.create("Branch B", "F2");
        assertEquals("Branch B", branch.getName());
        assertEquals("F2", branch.getFranchiseId());
        assertNotNull(branch.getProducts());
        assertTrue(branch.getProducts().isEmpty());
    }

    @Test
    void shouldAddProduct() {
        Branch branch = Branch.create("Branch", "F3");
        Product p = product("P1", 10);

        Branch updated = branch.addProduct(p);

        assertEquals(0, branch.getProducts().size());
        assertEquals(1, updated.getProducts().size());
        assertEquals("P1", updated.getProducts().get(0).getId());
    }

    @Test
    void shouldRemoveProduct() {
        Product p1 = product("P1", 5);
        Product p2 = product("P2", 8);
        Branch branch = Branch.builder()
                .name("Branch")
                .franchiseId("F4")
                .products(List.of(p1, p2))
                .build();

        Branch updated = branch.removeProduct("P1");

        assertEquals(2, branch.getProducts().size());
        assertEquals(1, updated.getProducts().size());
        assertEquals("P2", updated.getProducts().get(0).getId());
    }

    @Test
    void shouldUpdateProductStock() {
        Product p1 = product("P1", 5);
        Branch branch = Branch.builder()
                .name("Branch")
                .franchiseId("F5")
                .products(List.of(p1))
                .build();

        Branch updated = branch.updateProductStock("P1", 20);

        assertEquals(5, branch.getProducts().get(0).getStock());
        assertEquals(20, updated.getProducts().get(0).getStock());
    }
}