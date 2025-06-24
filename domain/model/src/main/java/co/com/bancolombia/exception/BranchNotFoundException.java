package co.com.bancolombia.exception;

public class BranchNotFoundException extends BusinessException {
  public BranchNotFoundException(String branchId) {
    super("BRANCH_NOT_FOUND", "Branch not found with id: " + branchId);
  }
}
