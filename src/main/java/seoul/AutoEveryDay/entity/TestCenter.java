package seoul.AutoEveryDay.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "name", nullable = false)
    String name;
    @Column(name = "address", nullable = false)
    String address;
}
