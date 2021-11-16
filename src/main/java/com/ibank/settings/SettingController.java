package com.ibank.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class SettingController {
    @Autowired
    private SettingService settingService;

    @GetMapping("/api/v1/user/settings")
    public List<Setting> index () {
        return settingService.getSettings();
    }

    /**
     * Modifica una setting
     * @param key
     * @return Ingredient
     */
    @PutMapping("/api/v1/user/setting/{key}")
    public Setting updateSetting (@PathVariable String key,
                                        @RequestBody Setting setting) {
        return settingService.updateSetting(key, setting);
    }
}
