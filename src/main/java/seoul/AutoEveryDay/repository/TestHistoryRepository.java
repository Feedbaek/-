package seoul.AutoEveryDay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seoul.AutoEveryDay.entity.TestHistory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TestHistoryRepository extends JpaRepository<TestHistory, Long> {
    List<TestHistory> findByTestCenterIdAndDate(Long testCenterId, LocalDate time);

    Optional<TestHistory> findByUserIdAndTestCenterIdAndDate(Long userId, Long testCenterId, LocalDate time);

    Long countByTestCenterIdAndDate(Long testCenterId, LocalDate time);
}
