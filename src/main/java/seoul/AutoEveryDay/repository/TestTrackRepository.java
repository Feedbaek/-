package seoul.AutoEveryDay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seoul.AutoEveryDay.entity.TestTrack;

import java.util.Optional;

@Repository
public interface TestTrackRepository extends JpaRepository<TestTrack, Long> {
    Optional<TestTrack> findByName(String trackName);

    boolean existsByName(String name);
}
