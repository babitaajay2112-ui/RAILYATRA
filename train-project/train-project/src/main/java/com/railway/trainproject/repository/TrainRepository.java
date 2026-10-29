
package com.railway.trainproject.repository;

import com.railway.trainproject.entity.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainRepository extends JpaRepository<Train, Long> {

    Optional<Train> findByTrainNumber(String trainNumber);

    List<Train> findBySourceIgnoreCaseAndDestinationIgnoreCase(
            String source,
            String destination
    );
}
