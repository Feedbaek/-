package seoul.AutoEveryDay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seoul.AutoEveryDay.entity.ChargeHistory;

import java.util.Collection;
import java.util.List;

@Repository
public interface ChargeHistoryRepository extends JpaRepository<ChargeHistory, Long> {
    List<ChargeHistory> findByUser_NameContaining(String search);

    List<ChargeHistory> findByUser_UserGroup_NameContaining(String search);

    List<ChargeHistory> findByCar_NumberContaining(String search);

    List<ChargeHistory> findByChargeSpot_NameContaining(String search);
}
