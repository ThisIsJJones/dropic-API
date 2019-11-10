package presentation.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class RegisterUserRequest {

    private String username;
    private String password;
}
