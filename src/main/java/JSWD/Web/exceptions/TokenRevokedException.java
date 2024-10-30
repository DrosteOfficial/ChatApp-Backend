package JSWD.Web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "The given token is revoked, try with a valid token", code = HttpStatus.BAD_REQUEST)
public class TokenRevokedException extends RuntimeException{
    public TokenRevokedException() {
        super("The given token is revoked, try with a valid token");
    }
}
