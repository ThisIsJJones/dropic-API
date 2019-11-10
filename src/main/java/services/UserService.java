package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import presentation.requests.DefaultUserCreationRequest;
import presentation.requests.RegisterUserRequest;
import repositories.UserRepository;
import repositories.models.DropicUser;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Integer addDefaultUser(DefaultUserCreationRequest defaultUser) {
        DropicUser dropicUser = new DropicUser().builder()
                .username(defaultUser.getUsername()).build();
        DropicUser savedDropicUser = userRepository.save(dropicUser);
        return savedDropicUser.getId();
    }

    public Integer addRegisteredUser(RegisterUserRequest userToRegister) {
//        DropicUser dropicUser = new DropicUser().builder()
//                .username(userToRegister.getUsername())
//                .passwordHash();
        return -1;
    }
}
