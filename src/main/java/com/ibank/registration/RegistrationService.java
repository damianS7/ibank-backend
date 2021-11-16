package com.ibank.registration;


import com.ibank.profile.Profile;
import com.ibank.profile.ProfileRepository;
import com.ibank.settings.Setting;
import com.ibank.settings.SettingRepository;
import com.ibank.user.User;
import com.ibank.user.UserRepository;
import com.ibank.user.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;

@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SettingRepository settingRepository;

    @Autowired
    public RegistrationService (
            UserRepository userRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            ProfileRepository profileRepository,
            SettingRepository settingRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.profileRepository = profileRepository;
        this.settingRepository = settingRepository;
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
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // Asignamos el rol por defecto
        user.setRole(UserRole.USER);

        // Guardamos el usuario
        userRepository.save(user);

        // Creamos datos por defecto ...

        // Perfil
        Profile profile = new Profile(null, user.getId(), 0, 0);
        profileRepository.save(profile);

        // Configuracion
        Setting s1 = new Setting(null, user.getId(), "meal1", "desayuno");
        settingRepository.save(s1);

        Setting s2 = new Setting(null, user.getId(), "meal2", "almuerzo");
        settingRepository.save(s2);

        Setting s3 = new Setting(null, user.getId(), "meal3", "merienda");
        settingRepository.save(s3);

        Setting s4 = new Setting(null, user.getId(), "meal4", "cena");
        settingRepository.save(s4);

        Setting s5 = new Setting(null, user.getId(), "meal5", "aperitivos");
        settingRepository.save(s5);

        return new RegistrationResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }
}

