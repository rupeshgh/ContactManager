package com.example.demo.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="User")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(unique = true)
    @Email(regexp = "^[A-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\\\.[A-Z0-9_!#$%&'*+/=?`{|}~^-]+↵\\n\" +\n" +
            "\t\t\t\t\")*@[A-Z0-9-]+(?:\\\\.[A-Z0-9-]+)*$")
    private String email;


    @Size(min=4,max=12,message = "Must be between 4 and 12 char")
    private String password;

    private String role;
    private String image;
    private boolean enabled;
    private String name;

    @Column(length = 1000)
    private String about;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER,mappedBy = "user")
    private List<Contact> contacts=new ArrayList<>();
}
