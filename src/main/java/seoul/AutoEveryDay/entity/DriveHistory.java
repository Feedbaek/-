package seoul.AutoEveryDay.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "drive_history")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriveHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "track_id", nullable = false)
    private Track track;

    // 주행 거리
    @Column(name = "distance", nullable = false)
    private Integer distance;
    // 주행 시간
    @Column(name = "time", nullable = false)
    private Integer time;
    // 평균 주행 속도
    @Column(name = "average_speed", nullable = false)
    private Integer averageSpeed;
    // 최고 주행 속도
    @Column(name = "max_speed", nullable = false)
    private Integer maxSpeed;
    // 주행 날짜
    @Column(name = "date", nullable = false)
    private LocalDate date;
}
