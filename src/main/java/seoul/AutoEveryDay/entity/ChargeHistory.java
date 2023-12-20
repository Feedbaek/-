package seoul.AutoEveryDay.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "charge_history")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChargeHistory {
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
    @JoinColumn(name = "charge_spot_id", nullable = false)
    private ChargeSpot chargeSpot;

    @Column(name = "amount", nullable = false)
    private Integer amount; // 리터 단위

    @CreatedDate
    @Column(name = "date", nullable = false)
    private Instant date;
}
