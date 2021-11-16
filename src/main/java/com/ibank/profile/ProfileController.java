package com.ibank.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/api/v1/user/profile")
    public Profile getProfile () {
        return profileService.getProfile();
    }

    @PutMapping("/api/v1/user/profile")
    public Profile updateProfile (@RequestBody Profile profile) {
        return profileService.updateProfile(profile);
    }
}
