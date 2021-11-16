package com.ibank.settings;

import com.ibank.user.User;
import com.ibank.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingService {

    @Autowired
    private SettingRepository settingRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Devuelve las opciones del usuario
     * @return List<Setting> Una lista con todas las opciones del usuario
     */
    public List<Setting> getSettings() {
        // Usuario logeado
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Entidad del usuario
        User user = userRepository.findByUsername((username)).orElseThrow();

        return settingRepository.findByUserId(user.getId());
    }

    /**
     * Modifica el valor de una opcion
     * @param key Nombre de la clave a modificar
     * @param settingRequest Contiene los datos
     * @return Setting La opcion modificada
     */
    public Setting updateSetting(String key, Setting settingRequest) {
        // Usuario logeado
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Entidad del usuario
        User user = userRepository.findByUsername((username)).orElseThrow();

        // Si no se encuentra lanza NoSuchElementException
        Setting setting = settingRepository.findByKeyAndUserId(key, user.getId()).orElseThrow();
        setting.setValue(settingRequest.getValue());

        return settingRepository.save(setting);
    }
}
