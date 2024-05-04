package JSWD.Web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "No token present in the request")
public class RegularTokenNotFoundException extends RuntimeException{
    public RegularTokenNotFoundException() {
        super("No token present in the request");
    }
}
