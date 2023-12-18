package seoul.AutoEveryDay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seoul.AutoEveryDay.entity.RentCar;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RentCarRepository extends JpaRepository<RentCar, Long> {
    Boolean existsByCar_IdAndReturnDateGreaterThanEqualAndPickupDateLessThanEqual(Long carId, LocalDate pickUpDate, LocalDate returnDate);
    List<RentCar> findByCar_IdAndReturnDateGreaterThanEqualAndPickupDateLessThanEqual(Long carId, LocalDate pickUpDate, LocalDate returnDate);
    List<RentCar> findByCar_IdAndReturnDateGreaterThanEqual(Long carId, LocalDate pickUpDate);
    Optional<RentCar> findByCar_IdAndPickupDateAndReturnDate(Long carId, LocalDate pickUpDate, LocalDate returnDate);
    Optional<RentCar> findByUser_IdAndCarIdAndPickupDateAndReturnDate(Long userId, Long carId, LocalDate pickUpDate, LocalDate returnDate);

    boolean existsByCar_IdAndPickupDateGreaterThanEqual(Long carId, LocalDate pickUpDate);

    List<RentCar> findByUser_Id(Long id);
}
