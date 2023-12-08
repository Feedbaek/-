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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_track_id", nullable = false)
    private TestTrack testTrack;

    @Column(name = "date", nullable = false)
    private LocalDate date;
}
