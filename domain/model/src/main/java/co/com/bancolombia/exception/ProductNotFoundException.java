package co.com.bancolombia.exception;

public class ProductNotFoundException extends BusinessException {
  public ProductNotFoundException(String productId) {
    super("PRODUCT_NOT_FOUND", "Product not found with id: " + productId);
  }
}
