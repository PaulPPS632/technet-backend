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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
                .sub(user.getSub())
                .name(user.getName())
                .given_name(user.getGiven_name())
                .family_name(user.getFamily_name())
                .picture(user.getPicture())
                .email(user.getEmail())
                .email_verified(user.isEmail_verified())
                .locale(user.getLocale())
                .password(user.getPassword())
                .tenantName(user.getTenantName())
                .regist(user.isRegist())
                .tiponegocio(user.getTiponegocio())
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
                .sub(user.getSub())
                .name(user.getName())
                .given_name(user.getGiven_name())
                .family_name(user.getFamily_name())
                .picture(user.getPicture())
                .email(user.getEmail())
                .email_verified(user.isEmail_verified())
                .locale(user.getLocale())
                .tenantName(user.getTenantName())
                .regist(user.isRegist())
                .tiponegocio(user.getTiponegocio())
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
                .sub(usuarioResponse.sub())
                .name(usuarioResponse.name())
                .given_name(usuarioResponse.given_name())
                .family_name(usuarioResponse.family_name())
                .picture(usuarioResponse.picture())
                .email(usuarioResponse.email())
                .email_verified(usuarioResponse.email_verified())
                .locale(usuarioResponse.locale())
                .tenantName(usuarioResponse.tenantName())
                .Regist(usuarioResponse.regist())
                .tiponegocio(usuarioResponse.tiponegocio())
                .rol(rol.orElseThrow())
                .build();
    }

    public void delete(String id) {
        userRepository.deleteById(id);
    }

    public LogicaNegocioUser Logica(String id) {
        return logicaNegocioUserRepository.findByUsuario(id).orElseThrow();
    }
}
