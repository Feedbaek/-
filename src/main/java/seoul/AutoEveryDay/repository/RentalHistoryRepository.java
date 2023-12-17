package seoul.AutoEveryDay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seoul.AutoEveryDay.entity.RentalHistory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RentalHistoryRepository extends JpaRepository<RentalHistory, Long> {
    Boolean existsByCar_IdAndReturnDateGreaterThanEqualAndPickupDateLessThanEqual(Long carId, LocalDate pickUpDate, LocalDate returnDate);
    List<RentalHistory> findByCar_IdAndReturnDateGreaterThanEqualAndPickupDateLessThanEqual(Long carId, LocalDate pickUpDate, LocalDate returnDate);
    List<RentalHistory> findByCar_IdAndReturnDateGreaterThanEqual(Long carId, LocalDate pickUpDate);
    Optional<RentalHistory> findByCar_IdAndPickupDateAndReturnDate(Long carId, LocalDate pickUpDate, LocalDate returnDate);
    Optional<RentalHistory> findByUser_IdAndCarIdAndPickupDateAndReturnDate(Long userId, Long carId, LocalDate pickUpDate, LocalDate returnDate);

    boolean existsByCar_IdAndPickupDateGreaterThanEqual(Long carId, LocalDate pickUpDate);
}
