package seoul.AutoEveryDay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seoul.AutoEveryDay.entity.DriveHistory;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DriveHistoryRepository extends JpaRepository<DriveHistory, Long> {
    Optional<DriveHistory> findByTrackIdAndDate(Long trackId, LocalDate time);

    Optional<DriveHistory> findByUserIdAndTrackIdAndDate(Long userId, Long trackId, LocalDate time);
}
