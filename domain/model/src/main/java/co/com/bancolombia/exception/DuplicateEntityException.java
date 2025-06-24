package co.com.bancolombia.exception;

public class DuplicateEntityException extends BusinessException {
  public DuplicateEntityException(String entity, String identifier) {
    super("DUPLICATE_" + entity.toUpperCase(), entity + " already exists with identifier: " + identifier);
  }
}
