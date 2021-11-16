package com.ibank.settings;

import javax.persistence.*;

@Entity
@Table(name = "settings")
public class Setting {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String key;

    @Column
    private String value;

    @Column
    private Long userId;


    public Setting() {

    }

    public Setting(Long id, Long userId, String key, String value) {
        this.id = id;
        this.key = key;
        this.value = value;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getUserId() {
        return userId;
    }
}
