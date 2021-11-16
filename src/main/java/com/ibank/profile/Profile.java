package com.ibank.profile;

import javax.persistence.*;

@Entity
@Table(name = "profiles")
public class Profile {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long userId;

    @Column
    private int age;

    @Column
    private int height;

    public Profile() {

    }

    public Profile(Long id, Long userId, int age, int height) {
        this.id = id;
        this.userId = userId;
        this.age = age;
        this.height = height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public int getHeight() {
        return height;
    }

}
