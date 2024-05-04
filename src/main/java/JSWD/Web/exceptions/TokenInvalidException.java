package JSWD.Web.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "The given token is invalid, try with a valid token", code = org.springframework.http.HttpStatus.BAD_REQUEST)
public class TokenInvalidException extends RuntimeException{
    public TokenInvalidException() {
        super("The given token is invalid, try with a valid token");
    }
}
