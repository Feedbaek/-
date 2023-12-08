package seoul.AutoEveryDay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seoul.AutoEveryDay.entity.TestHistory;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface TestHistoryRepository extends JpaRepository<TestHistory, Long> {
    Optional<TestHistory> findByTestTrackIdAndDate(Long testTrackId, LocalDate time);

    Optional<TestHistory> findByUserIdAndTestTrackIdAndDate(Long userId, Long testTrackId, LocalDate time);
}
