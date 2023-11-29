package seoul.AutoEveryDay.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Entity
@Table(name = "role")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "roles_privileges",
        joinColumns = @JoinColumn(
                name = "role_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(
                name = "privilege_id", referencedColumnName = "id"))
    private Collection<Privilege> privileges;
}
