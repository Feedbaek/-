package seoul.AutoEveryDay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seoul.AutoEveryDay.entity.ChargeSpot;

import java.util.Optional;

public interface ChargeSpotRepository extends JpaRepository<ChargeSpot, Long> {
    Optional<ChargeSpot> findByName(String name);

    boolean existsByName(String name);
}
