package co.com.bancolombia.exception;

public class InvalidStockException extends BusinessException {
  public InvalidStockException(int stock) {
    super("INVALID_STOCK", "Invalid stock value: " + stock + ". Stock must be non-negative.");
  }
}
