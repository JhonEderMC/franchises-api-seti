package co.com.bancolombia.exception;

public class FranchiseNotFoundException extends BusinessException {
  public FranchiseNotFoundException(String franchiseId) {
    super("FRANCHISE_NOT_FOUND", "Franchise not found with id: " + franchiseId);
  }
}
