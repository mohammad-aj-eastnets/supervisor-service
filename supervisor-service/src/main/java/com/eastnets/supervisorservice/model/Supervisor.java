package com.eastnets.supervisorservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "supervisor")
public class Supervisor {
    @Id
    private Long id;
    private String name;
    private String username;
    private String password;
}
