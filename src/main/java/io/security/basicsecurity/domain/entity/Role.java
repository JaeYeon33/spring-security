package io.security.basicsecurity.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"users", "resourcesSet"})
@EqualsAndHashCode(of = "id")
public class Role implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_id")
    private Long id;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "role_desc")
    private String roleDesc;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roleSet")
    @OrderBy("orderNum desc")
    private Set<Resources> resourceSet = new LinkedHashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "userRoles")
    private Set<Account> accounts = new HashSet<>();
}
