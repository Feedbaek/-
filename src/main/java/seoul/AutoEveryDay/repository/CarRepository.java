package seoul.AutoEveryDay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seoul.AutoEveryDay.dto.CarDto;
import seoul.AutoEveryDay.entity.Car;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    boolean existsByNumber(String number);

    Optional<Car> findByNumber(String number);

    List<Car> findByNumberContaining(String number);

    List<Car> findByCarModel_IdAndNumberContaining(Long carModelId, String number);

    List<Car> findByCarModel_Id(Long carModel);
}
