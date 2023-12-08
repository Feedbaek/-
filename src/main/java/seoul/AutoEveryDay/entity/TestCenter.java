package seoul.AutoEveryDay.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "test_center")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestCenter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    String name;
    @Column(name = "address", nullable = false)
    String address;
    @OneToMany(mappedBy = "testCenter", cascade = CascadeType.ALL)
    private List<TestHistory> testHistories;
}
