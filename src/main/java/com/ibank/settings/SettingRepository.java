package com.ibank.settings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SettingRepository extends JpaRepository<Setting, String>  {
    List<Setting> findByUserId(Long userId);
    Optional<Setting> findByKeyAndUserId(String key, Long userId);
}
