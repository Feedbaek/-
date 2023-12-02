package seoul.AutoEveryDay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seoul.AutoEveryDay.entity.RentalHistory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RentalHistoryRepository extends JpaRepository<RentalHistory, Long> {
    Optional<RentalHistory>findByReturnDateGreaterThanAndPickupDateLessThan(LocalDate pickUpDate, LocalDate returnDate);
}
