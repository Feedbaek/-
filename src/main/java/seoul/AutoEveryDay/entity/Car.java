package seoul.AutoEveryDay.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "available", nullable = false)
    private boolean available;

    @Column(name = "status")
    private String status;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;
}
