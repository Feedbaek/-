package seoul.AutoEveryDay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seoul.AutoEveryDay.entity.DriveHistory;

@Repository
public interface DriveHistoryRepository extends JpaRepository<DriveHistory, Long>  {
}
