package io.security.basicsecurity.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"userRoles"})
@EqualsAndHashCode(of = "id")
public class Account implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String email;
    private int age;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Role> userRoles = new HashSet<>();
}
