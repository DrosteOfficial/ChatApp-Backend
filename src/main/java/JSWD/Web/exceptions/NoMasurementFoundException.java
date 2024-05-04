package JSWD.Web.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Could not found any measurements")
public class NoMasurementFoundException extends RuntimeException {
    public NoMasurementFoundException() {
        super("Could not found any measurements");
    }
}

