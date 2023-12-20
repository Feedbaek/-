package seoul.AutoEveryDay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seoul.AutoEveryDay.entity.ChargeHistory;

@Repository
public interface ChargeHistoryRepository extends JpaRepository<ChargeHistory, Long> {
}
