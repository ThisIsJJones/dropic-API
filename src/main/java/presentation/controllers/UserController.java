package presentation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import presentation.requests.DefaultUserCreationRequest;
import presentation.requests.RegisterUserRequest;
import services.UserService;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/addDefault", method = RequestMethod.POST)
    public Integer addDefaultUser(@RequestBody DefaultUserCreationRequest user) {
        System.out.println("ADDING USER");
        if (user == null) {
            return -1;
        }
        return userService.addDefaultUser(user);
    }

    @RequestMapping(value = "/register")
    public Integer registerUser(@RequestBody RegisterUserRequest user) {
        if (user == null) {
            return -1;
        }
        return userService.addRegisteredUser(user);
    }

}
