package io.security.basicsecurity.domain.entity;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
@Builder
@Table(name = "RESOURCES")
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"roleSet"})
@EntityListeners(value = {AuditingEntityListener.class})
public class Resources implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "resource_id")
    private Long id;

    @Column(name = "resource_name")
    private String resourceName;

    @Column(name = "http_method")
    private String httpMethod;

    @Column(name = "order_num")
    private int orderNum;

    @Column(name = "resource_type")
    private String resourceType;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Role> roleSet = new HashSet<>();
}
