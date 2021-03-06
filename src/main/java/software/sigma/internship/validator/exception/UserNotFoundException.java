package software.sigma.internship.validator.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User id not found: " + id);
    }
}
