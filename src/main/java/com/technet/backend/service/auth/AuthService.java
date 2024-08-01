package com.technet.backend.service.auth;

import com.technet.backend.exception.ResourceNotFoundException;
import com.technet.backend.model.dto.auth.AuthResponse;
import com.technet.backend.model.dto.auth.LoginRequest;
import com.technet.backend.model.dto.users.UserAuthDto;
import com.technet.backend.model.entity.users.Rol;
import com.technet.backend.model.entity.users.User;
import com.technet.backend.repository.users.RolRepository;
import com.technet.backend.repository.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final RolRepository rolRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthResponse login(LoginRequest loginRequest) {
        String username = loginRequest.username();
        String password = loginRequest.password();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
        User user = userRepository.findByUsername(username).orElseThrow();
        UserDetails userdetails = user;
        String token = jwtService.getToken(userdetails);
        return AuthResponse.builder().token(token).rol(user.getRol().getNombre()).username(user.getUsername()).build();
    }

    public AuthResponse register(UserAuthDto userAuthDto) {
        Optional<Rol> rol = rolRepository.findByNombre(userAuthDto.rol());
        if(rol.isEmpty()){
            throw new ResourceNotFoundException("Rol no encontrado: " + userAuthDto.rol());
        }
        User user = User.builder()
                .username(userAuthDto.username())
                .email(userAuthDto.email())
                .password(passwordEncoder.encode(userAuthDto.password()))
                .Regist(true)
                .rol(rol.get())
                .build();
        userRepository.save(user);
        return AuthResponse.builder()
                .token(jwtService.getToken(user))
                .rol(userAuthDto.rol())
                .username(userAuthDto.username())
                .build();
    }
    public Map<String, Object> validate(String token) {
        Map<String, Object> response = jwtService.validate(token);
        response.put("rol",userRepository.findByUsername((String) response.get("username")).orElseThrow().getRol().getNombre());
        return response;
    }
}
