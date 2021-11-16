package com.ibank.profile;

import com.ibank.user.User;
import com.ibank.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Devuelve todas las comidas del usuario logeado
     * @return
     */
    public Profile getProfile() {
        // Usuario logeado
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Entidad del usuario
        User user = userRepository.findByUsername((username)).orElseThrow();

        // Perfil del usuario
        Profile profile = profileRepository.findByUserId(user.getId()).orElseThrow();

        return profile;
    }

    /**
     * Actualiza el perfil del usuario
     * @param requestProfile Datos de perfil enviados por el usuario
     * @return Devuelve el perfil modificado
     */
    public Profile updateProfile(Profile requestProfile) {
        // Usuario logeado
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Entidad del usuario
        User user = userRepository.findByUsername((username)).orElseThrow();

        // Perfil del usuario
        Profile profile = profileRepository.findByUserId(user.getId()).orElseThrow();

        // Modificamos el perfil y guardamos
        profile.setAge(requestProfile.getAge());
        profile.setHeight(requestProfile.getHeight());
        return profileRepository.save(profile);
    }

}
