package co.com.bancolombia.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void shouldBuildProductWithBuilder() {
        Product product = Product.builder()
                .id("1")
                .name("Product A")
                .stock(10)
                .branchId("B1")
                .build();

        assertEquals("1", product.getId());
        assertEquals("Product A", product.getName());
        assertEquals(10, product.getStock());
        assertEquals("B1", product.getBranchId());
    }

    @Test
    void shouldCreateProductWithStaticMethod() {
        Product product = Product.create("Product B", 5, "B2");

        assertNull(product.getId());
        assertEquals("Product B", product.getName());
        assertEquals(5, product.getStock());
        assertEquals("B2", product.getBranchId());
    }

    @Test
    void shouldUseWithMethods() {
        Product product = Product.builder()
                .id("2")
                .name("Product C")
                .stock(7)
                .branchId("B3")
                .build();

        Product updated = product.withStock(20).withName("Product D");

        assertEquals("2", updated.getId());
        assertEquals("Product D", updated.getName());
        assertEquals(20, updated.getStock());
        assertEquals("B3", updated.getBranchId());
        // Original remains unchanged
        assertEquals("Product C", product.getName());
        assertEquals(7, product.getStock());
    }
}