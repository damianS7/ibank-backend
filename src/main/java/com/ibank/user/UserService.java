package com.ibank.user;

import com.ibank.auth.AuthenticationFailedException;
import com.ibank.user.exception.EmailTakenException;
import com.ibank.user.exception.UsernameTakenException;
import com.ibank.user.http.RegistrationResponse;
import com.ibank.user.http.UserUpdateRequest;
import com.ibank.user.http.UserUpdateResponse;
import com.ibank.utils.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

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
        if (!PasswordEncoder.encoder().matches(request.getOldPassword(), user.getPassword())) {
            // Si las password no coinciden ...
            throw new AuthenticationFailedException("Incorrect password");
        }

        String encodedPassword = PasswordEncoder.encode(request.getNewPassword());
        // Modificamos el usuario
        user.setUsername(request.getUsername());
        user.setPassword(encodedPassword);
        user.setEmail(request.getEmail());

        // Guardamos los cambios
        userRepository.save(user);
        return new UserUpdateResponse(user.getUsername(), user.getEmail());
    }

    /**
     * Metodo para dar de alta nuevos usuarios
     * @param user
     * @return devuelve el usuario creado o una excepcion
     */
    public RegistrationResponse register(User user) throws EmailTakenException, UsernameTakenException {

        // Comprobamos que no exista otro usuario con el mismo nombre
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            // El nombre de usuario esta en uso
            throw new UsernameTakenException("Este nombre de usuario ya esta en uso.");
        }

        // Aqui se comprueba si el email ya ha sido usado
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            // El correo ya esta en uso ...
            throw new EmailTakenException("Este correo ya esta en uso.");
        }

        // Ciframos el password
        String encodedPassword = PasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // Asignamos el rol por defecto
        user.setRole(UserRole.USER);

        // Guardamos el usuario
        User savedUser = userRepository.save(user);

        return new RegistrationResponse(savedUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow( () -> {
            throw new UsernameNotFoundException("Este nombre no ha sido encontrado.");
        });
    }
}
