package JSWD.Web.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "The given token is revoked, try with a valid token", code = org.springframework.http.HttpStatus.BAD_REQUEST)
public class TokenRevokedException extends RuntimeException{
    public TokenRevokedException() {
        super("The given token is revoked, try with a valid token");
    }
}
