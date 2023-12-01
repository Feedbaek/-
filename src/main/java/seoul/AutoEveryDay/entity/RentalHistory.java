package seoul.AutoEveryDay.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "rental_history")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RentalHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "pickup_date")
    private LocalDateTime pickupDate;

    @Column(name = "return_date")
    private LocalDateTime returnDate;
}
