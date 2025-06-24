package co.com.bancolombia.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductStockTest {

    @Test
    void shouldBuildProductStockWithBuilder() {
        ProductStock stock = ProductStock.builder()
                .productId("P1")
                .productName("Product A")
                .branchId("B1")
                .branchName("Branch X")
                .stock(15)
                .build();

        assertEquals("P1", stock.getProductId());
        assertEquals("Product A", stock.getProductName());
        assertEquals("B1", stock.getBranchId());
        assertEquals("Branch X", stock.getBranchName());
        assertEquals(15, stock.getStock());
    }

    @Test
    void shouldSetAndGetFields() {
        ProductStock stock = ProductStock.builder().build();
        stock.setProductId("P2");
        stock.setProductName("Product B");
        stock.setBranchId("B2");
        stock.setBranchName("Branch Y");
        stock.setStock(20);

        assertEquals("P2", stock.getProductId());
        assertEquals("Product B", stock.getProductName());
        assertEquals("B2", stock.getBranchId());
        assertEquals("Branch Y", stock.getBranchName());
        assertEquals(20, stock.getStock());
    }

    @Test
    void shouldTestEqualsAndHashCode() {
        ProductStock s1 = ProductStock.builder()
                .productId("P3")
                .productName("Product C")
                .branchId("B3")
                .branchName("Branch Z")
                .stock(5)
                .build();

        ProductStock s2 = ProductStock.builder()
                .productId("P3")
                .productName("Product C")
                .branchId("B3")
                .branchName("Branch Z")
                .stock(5)
                .build();

        ProductStock s3 = ProductStock.builder()
                .productId("P4")
                .productName("Product D")
                .branchId("B4")
                .branchName("Branch W")
                .stock(10)
                .build();

        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
        assertNotEquals(s1, s3);
    }
}