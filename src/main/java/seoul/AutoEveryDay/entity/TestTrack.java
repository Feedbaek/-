package seoul.AutoEveryDay.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "test_track")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestTrack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    String name;
    @Column(name = "description")
    String description;
    @OneToMany(mappedBy = "testTrack", cascade = CascadeType.ALL)
    private List<TestHistory> testHistories;
}
