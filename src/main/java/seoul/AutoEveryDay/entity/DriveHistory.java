package seoul.AutoEveryDay.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "drive_history")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriveHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;
    @ManyToOne
    @JoinColumn(name = "track_id", nullable = false)
    private Track track;
    @CreationTimestamp
    @Column(name = "date", nullable = false)
    private Instant date;
}
