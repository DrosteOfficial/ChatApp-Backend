package JSWD.Web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Could not found an interface by id")
public class InterfaceNotFoundException extends RuntimeException{
    public InterfaceNotFoundException(UUID uuid) {
        super("Could not found an interface by id: " + uuid.toString());
    }
}
