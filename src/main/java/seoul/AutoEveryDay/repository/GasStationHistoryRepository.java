package seoul.AutoEveryDay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seoul.AutoEveryDay.entity.GasStationHistory;

@Repository
public interface GasStationHistoryRepository extends JpaRepository<GasStationHistory, Long> {
}
