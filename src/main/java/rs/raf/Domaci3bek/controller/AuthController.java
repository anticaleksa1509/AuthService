package rs.raf.Domaci3bek.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.raf.Domaci3bek.model.User;
import rs.raf.Domaci3bek.request.TokenRequestDTO;
import rs.raf.Domaci3bek.service.TokenService;
import rs.raf.Domaci3bek.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    TokenService tokenService;
    @Autowired
    UserService userService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,value = "/login")
    public ResponseEntity<?> login(@RequestBody TokenRequestDTO tokenRequestDTO){
        try {
            userService.login(tokenRequestDTO);
            return new ResponseEntity<>(userService.login(tokenRequestDTO), HttpStatus.OK);
        }catch (EntityNotFoundException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
}
