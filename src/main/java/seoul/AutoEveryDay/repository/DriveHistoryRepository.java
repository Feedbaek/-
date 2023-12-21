package seoul.AutoEveryDay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seoul.AutoEveryDay.entity.DriveHistory;

import java.util.Collection;
import java.util.List;

@Repository
public interface DriveHistoryRepository extends JpaRepository<DriveHistory, Long>  {
    List<DriveHistory> findByUser_NameContaining(String search);

    List<DriveHistory> findByCar_NumberContaining(String search);

    List<DriveHistory> findByCar_CarModel_NameContaining(String search);

    List<DriveHistory> findByTrack_NameContaining(String search);
}
