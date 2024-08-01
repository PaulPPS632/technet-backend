package com.technet.backend.service.User;

import com.technet.backend.model.dto.globales.UserInfo;
import com.technet.backend.model.dto.users.PrivilegioResponse;
import com.technet.backend.model.dto.users.RolResponse;
import com.technet.backend.model.dto.users.UserAuthDto;
import com.technet.backend.model.dto.users.UserResponse;
import com.technet.backend.model.entity.users.LogicaNegocioUser;
import com.technet.backend.model.entity.users.Privilegio;
import com.technet.backend.model.entity.users.Rol;
import com.technet.backend.model.entity.users.User;
import com.technet.backend.repository.users.LogicaNegocioUserRepository;
import com.technet.backend.repository.users.RolRepository;
import com.technet.backend.repository.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SQLService sqlService;
    private final JdbcTemplate jdbcTemplate;
    private final RolRepository rolRepository;
    private final com.technet.backend.repository.users.privilegioRepository privilegioRepository;
    private final LogicaNegocioUserRepository logicaNegocioUserRepository;
    //@Transactional
    public UserResponse Regist(UserAuthDto usuario) {
        try {
            Optional<User> optionalUser = userRepository.findByEmail(usuario.email());
            if(optionalUser.isPresent()) {
                optionalUser.get().setRegist(true);
                return maptoUserResponse(optionalUser.get());
            }else{
                return maptoUserResponse(crear(usuario));
            }
        }catch (Exception e) {
            // Manejar la excepción adecuadamente
            throw new RuntimeException("Error al registrar usuario", e);
        }

    }
    private User crear(UserAuthDto usuario){
        User user = new User();
        Rol rol = rolRepository.findById(1L).orElseThrow(() -> new RuntimeException("Rol not found"));
        List<Long> privilegioIds =new ArrayList<>();
        privilegioIds.add(1L);
        privilegioIds.add(2L);
        privilegioIds.add(3L);
        privilegioIds.add(4L);
        privilegioIds.add(5L);
        List<Privilegio> privilegios = privilegioRepository.findAllById(privilegioIds);
        rol.setPrivilegios(privilegios);
        rolRepository.save(rol);
        user.setEmail(usuario.email());
        user.setPassword(usuario.password());
        user.setRol(rol);

        User guardado = userRepository.save(user);
        //logicaNegocioUserRepository.save(LogicaNegocioUser.builder().usuario(guardado.getId()).metaventas(8000D).build());
        return guardado;
    }
    private UserAuthDto maptoUserAuthDTO(User user){
        return UserAuthDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .regist(user.isRegist())
                .build();
    }
    public UserResponse Login(String email, String password) {
        return maptoUserResponse(userRepository.findByEmailAndPassword(email,password).orElseThrow());
    }

    public List<UserResponse> getAll() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::maptoUserResponse).toList();
    }
    private UserResponse maptoUserResponse(User user){
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getUsername())
                .email(user.getEmail())
                .regist(user.isRegist())
                .rol(maptoRolResponse(user.getRol()))
                .build();
    }
    private RolResponse maptoRolResponse(Rol rol){
        if(rol != null){
            return RolResponse.builder()
                    .id(rol.getId())
                    .nombre(rol.getNombre())
                    .descripcion(rol.getDescripcion())
                    .privilegios(rol.getPrivilegios().stream().map(this::maptoPrivilegioResponse).toList())
                    .build();
        }
        return RolResponse.builder().build();
    }
    private PrivilegioResponse maptoPrivilegioResponse(Privilegio privilegio){
        if(privilegio != null){
            return PrivilegioResponse.builder()
                    .id(privilegio.getId())
                    .nombre(privilegio.getNombre())
                    .descripcion(privilegio.getDescripcion())
                    .build();
        }
        return PrivilegioResponse.builder().build();
    }


    public List<RolResponse> getAllRoles() {
        return rolRepository.findAll().stream().map(this::maptoRolResponse).toList();
    }

    public void AsignarRol(UserResponse usuario) {
        Optional<User> user = userRepository.findById(usuario.id());
        user.orElseThrow().setRol(rolRepository.findById(usuario.rol().id()).orElseThrow());
        userRepository.save(user.get());
    }
    private User maptoUser(UserResponse usuarioResponse){
        Optional<Rol> rol = rolRepository.findById(usuarioResponse.rol().id());
        return User.builder()
                .id(usuarioResponse.id())
                .username(usuarioResponse.name())
                .email(usuarioResponse.email())
                .Regist(usuarioResponse.regist())
                .rol(rol.orElseThrow())
                .build();
    }

    public void delete(String id) {
        userRepository.deleteById(id);
    }

    public LogicaNegocioUser Logica(String id) {
        return logicaNegocioUserRepository.findByUsuario(id).orElseThrow();
    }

    public ResponseEntity<UserResponse> getUserByUsername(String username) {
        if(!username.isEmpty()){
            return ResponseEntity.ok(maptoUserResponse(userRepository.findByUsername(username).orElseThrow()));
        }else {
            return ResponseEntity.status(500).body(UserResponse.builder().build());
        }
    }
}
