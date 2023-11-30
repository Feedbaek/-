package seoul.AutoEveryDay.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "gas_station_history")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GasStationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "charge_spot_id", nullable = false)
    private ChargeSpot chargeSpot;

    @Column(name = "amount", nullable = false)
    private Long amount; // 리터 단위
}
