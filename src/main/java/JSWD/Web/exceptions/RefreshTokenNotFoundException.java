package JSWD.Web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Refresh token not found", code = HttpStatus.NOT_FOUND)
public class RefreshTokenNotFoundException extends RuntimeException{
    public RefreshTokenNotFoundException(String token) {
        super("Refresh token not found: " + token);
    }
}
