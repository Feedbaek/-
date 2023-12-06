package seoul.AutoEveryDay.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "test_history")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "test_center_id", nullable = false)
    private TestCenter testCenter;

    @Column(name = "time", nullable = false)
    private LocalDate time;
}
