package seoul.AutoEveryDay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seoul.AutoEveryDay.entity.ReserveHistory;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ReserveHistoryRepository extends JpaRepository<ReserveHistory, Long> {
    Optional<ReserveHistory> findByTrackIdAndDate(Long trackId, LocalDate time);

    Optional<ReserveHistory> findByUserIdAndTrackIdAndDate(Long userId, Long trackId, LocalDate time);

    boolean existsByTrackId(Long trackId);
}
