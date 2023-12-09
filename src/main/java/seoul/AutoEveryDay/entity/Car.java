package seoul.AutoEveryDay.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "car")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number", nullable = false, unique = true)
    private String number;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "car_model_id", nullable = false)
    private CarModel carModel;

    @Column(name = "status")
    private String status;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    private List<RentalHistory> rentalHistories;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    private List<ChargeHistory> gasStationHistories;
}
