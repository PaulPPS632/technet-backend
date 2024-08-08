package com.technet.backend.controller.user;

import com.technet.backend.model.dto.globales.UserInfo;
import com.technet.backend.model.dto.users.*;
import com.technet.backend.model.entity.users.LogicaNegocioUser;
import com.technet.backend.model.entity.users.User;
import com.technet.backend.service.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/regist")
    public UserResponse Regist(@RequestBody UserAuthDto usuario){
        return userService.Regist(usuario);
    }
    private User maptoUser(UserInfo userInfo, User user){
        return new User().builder()
                .username(userInfo.name())
                .email(userInfo.email())
                .Regist(false)
                .build();
    }
    @GetMapping("/login")
    public UserResponse Login(@RequestParam String email, @RequestParam String password){
        return userService.Login(email, password);
    }
    @GetMapping
    public List<UserResponse> GetAll(){

        return userService.getAll();
    }
    @GetMapping("/byusername/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable("username") String username){
        return userService.getUserByUsername(username);
    }
    @GetMapping("/dashboard")
    public ResponseEntity<List<UserResponseV2>> GetUsuarioDashboard(){
        return userService.getUserDashboard();
    }
    @GetMapping("/roles")
    public List<RolResponse> GetAllRoles(){
        return userService.getAllRoles();
    }
    @PutMapping("/asignarrol")
    public void putUsuario(@RequestBody UserRequest usuario){
        userService.AsignarRol(usuario);
    }
    @DeleteMapping("/{id}")
    public void deleteUsuario(@PathVariable("id") String id){
        userService.delete(id);
    }
    @GetMapping("/logica/{id}")
    public LogicaNegocioUser logica(@PathVariable("id") String id){
        return userService.Logica(id);
    }
}
