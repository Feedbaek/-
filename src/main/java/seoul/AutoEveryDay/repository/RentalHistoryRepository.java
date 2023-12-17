package seoul.AutoEveryDay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seoul.AutoEveryDay.entity.RentalHistory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RentalHistoryRepository extends JpaRepository<RentalHistory, Long> {
    Boolean existsByCarIdAndReturnDateGreaterThanEqualAndPickupDateLessThanEqual(Long carId, LocalDate pickUpDate, LocalDate returnDate);
    List<RentalHistory> findByCarIdAndReturnDateGreaterThanEqualAndPickupDateLessThanEqual(Long carId, LocalDate pickUpDate, LocalDate returnDate);
    List<RentalHistory> findByCarIdAndReturnDateGreaterThanEqual(Long carId, LocalDate pickUpDate);
    Optional<RentalHistory> findByCarIdAndPickupDateAndReturnDate(Long carId, LocalDate pickUpDate, LocalDate returnDate);
    Optional<RentalHistory> findByUserIdAndCarIdAndPickupDateAndReturnDate(Long userId, Long carId, LocalDate pickUpDate, LocalDate returnDate);
}
