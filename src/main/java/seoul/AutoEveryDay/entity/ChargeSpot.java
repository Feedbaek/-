package seoul.AutoEveryDay.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "charge_spot")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChargeSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    String name;

    @OneToMany(mappedBy = "chargeSpot", cascade = CascadeType.ALL)
    private List<GasStationHistory> gasStationHistories;
}
