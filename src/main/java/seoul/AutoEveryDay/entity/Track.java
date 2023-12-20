package seoul.AutoEveryDay.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "track")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    String name;
    @Column(name = "description")
    String description;

    @Column(name = "image")
    String image;

    @OneToMany(mappedBy = "track", cascade = CascadeType.ALL)
    private List<ReserveTrack> reserveHistories;
}
