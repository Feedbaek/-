package seoul.AutoEveryDay.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_group_id")
    private UserGroup userGroup;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "users_roles",
        joinColumns = @JoinColumn(
                name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(
                name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ChargeHistory> gasStationHistories;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<RentalHistory> rentalHistories;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<TestHistory> testHistories;
}
