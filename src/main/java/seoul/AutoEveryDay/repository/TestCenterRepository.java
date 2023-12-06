package seoul.AutoEveryDay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seoul.AutoEveryDay.entity.TestCenter;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestCenterRepository extends JpaRepository<TestCenter, Long> {
    Optional<TestCenter> findByName(String centerName);

    boolean existsByName(String name);
}
