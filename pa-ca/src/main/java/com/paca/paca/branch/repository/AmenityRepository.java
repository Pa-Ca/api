package com.paca.paca.branch.repository;

import com.paca.paca.branch.model.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AmenityRepository extends JpaRepository<Amenity, Long> {
    @Query(value = "SELECT * FROM amenity " +
            "WHERE SIMILARITY(?1, amenity.name) > 0.1 OR ?1 % ANY(STRING_TO_ARRAY(amenity.name,' '))"
            , nativeQuery = true)
    List<Amenity> findAllMatching(String word);
}
