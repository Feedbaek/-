package seoul.AutoEveryDay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seoul.AutoEveryDay.entity.ReserveTrack;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ReserveTrackRepository extends JpaRepository<ReserveTrack, Long> {
    Optional<ReserveTrack> findByTrackIdAndDate(Long trackId, LocalDate time);

    Optional<ReserveTrack> findByUserIdAndTrackIdAndDate(Long userId, Long trackId, LocalDate time);

    boolean existsByTrackId(Long trackId);
}
