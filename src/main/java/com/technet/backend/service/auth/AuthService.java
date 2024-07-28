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
        UserDetails user = userRepository.findByUsername(loginRequest.username()).orElseThrow();
        String token = jwtService.getToken(user);
        return AuthResponse.builder().token(token).build();
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
                .build();
    }
    public Map<String, Object> validate(String token) {
        return jwtService.validate(token);
    }

}
