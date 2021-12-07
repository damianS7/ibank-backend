package com.ibank.user;

import com.ibank.user.exception.EmailTakenException;
import com.ibank.user.exception.UsernameTakenException;
import com.ibank.user.http.UserSignupRequest;
import com.ibank.user.http.UserUpdateRequest;
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

    /**
     * Modifica los datos de un usuario
     *
     * @param request La peticion con los datos de usuario a modificar
     * @return El usuario actualizado
     */
    public User updateLoggedUser(UserUpdateRequest request) {
        // Leemos el nombre de usuario desde el sistema (token?), no desde la peticion
        // Cambiar a id ???
        // User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Long currentUserId = loggedUser.getId()
        String currentUsername = "";
        //try {
        //    currentUsername = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //} catch (Exception e) {
        //}

        if (!currentUsername.equals(request.username)) {
            throw new IllegalStateException("Attemp to modify a different user.");
        }

        // Guardamos los cambios
        return this.updateUser(currentUsername, request.username, request.email, request.oldPassword, request.newPassword);
    }

    User updateUser(String currentUsername, String newUsername, String newEmail, String oldPassword, String newPassword) {

        // Buscamos el usuario en la db
        User user = userRepository.findByUsername(currentUsername).orElseThrow();

        // Antes de cambiar comprobamos que las password antiguas coincidan
        if (!PasswordEncoder.encoder().matches(oldPassword, user.getPassword())) {
            // Si las password no coinciden ...
            throw new IllegalStateException("Password does not match! : " + oldPassword + " != " + user.getPassword());
        }

        // Password cifrado
        String encodedPassword = PasswordEncoder.encode(newPassword);

        // Modificamos el usuario
        user.setUsername(newUsername);
        user.setPassword(encodedPassword);
        user.setEmail(newEmail);

        // Guardamos los cambios
        return userRepository.save(user);
    }

    /**
     * Metodo para dar de alta nuevos usuarios
     *
     * @param signupRequest Datos del usuario a registrar
     * @return devuelve el usuario creado o una excepcion
     */
    public User createUser(UserSignupRequest signupRequest) {
        // Comprobamos que no exista otro usuario con el mismo nombre
        if (userRepository.findByUsername(signupRequest.username).isPresent()) {
            // El nombre de usuario esta en uso
            throw new UsernameTakenException("Este nombre de usuario ya esta en uso.");
        }

        // Aqui se comprueba si el email ya ha sido usado
        if (userRepository.findByEmail(signupRequest.email).isPresent()) {
            // El correo ya esta en uso ...
            throw new EmailTakenException("Este correo ya esta en uso.");
        }

        // Ciframos el password
        String encodedPassword = PasswordEncoder.encode(signupRequest.password);

        // Creamos la entidad a guardar en la db
        User user = new User(null, signupRequest.username, signupRequest.email, encodedPassword);

        // Guardamos el usuario
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> {
            throw new UsernameNotFoundException("Este nombre no ha sido encontrado.");
        });
    }
}
