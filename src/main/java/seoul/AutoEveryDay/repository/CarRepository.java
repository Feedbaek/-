package seoul.AutoEveryDay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seoul.AutoEveryDay.entity.Car;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    boolean existsByNumber(String number);
}
