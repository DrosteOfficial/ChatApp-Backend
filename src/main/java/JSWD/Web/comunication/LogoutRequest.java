package JSWD.Web.comunication;

import JSWD.Web.model.security.RegularToken;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LogoutRequest {
    @NotBlank
    @Size(max = 500)
    private String RefreshToken;
    public LogoutRequest() {
    }
    public LogoutRequest(String refreshToken) {
        RefreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return RefreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        RefreshToken = refreshToken;
    }
}
