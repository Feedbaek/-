package seoul.AutoEveryDay.entity;

import jakarta.persistence.*;
import lombok.*;

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
}
