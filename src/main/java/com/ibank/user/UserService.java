package com.ibank.user;

import com.ibank.auth.AuthenticationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Modifica los datos de un usuario
    public UserUpdateResponse update(UserUpdateRequest request) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Obtenemos el nombre del usuario logeado que envia la peticion
        User user = userRepository.findByUsername(username).orElseThrow();

        // Antes de cambiar comprobamos que las password antiguas coincidan
        if (!bCryptPasswordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            // Si las password no coinciden ...
            throw new AuthenticationFailedException("Incorrect password");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(request.getNewPassword());
        // Modificamos el usuario
        user.setUsername(request.getUsername());
        user.setPassword(encodedPassword);
        user.setEmail(request.getEmail());

        // Guardamos los cambios
        userRepository.save(user);
        return new UserUpdateResponse(user.getUsername(), user.getEmail());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow( () -> {
            throw new UsernameNotFoundException("Este nombre no ha sido encontrado.");
        });
    }
}
