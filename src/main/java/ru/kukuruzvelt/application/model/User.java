package ru.kukuruzvelt.application.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="public.Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String name;
    private String password;
    private String role;
}
