package seoul.AutoEveryDay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seoul.AutoEveryDay.entity.RentalHistory;

@Repository
public interface RentalHistoryRepository extends JpaRepository<RentalHistory, Long> {
}
