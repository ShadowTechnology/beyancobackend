package com.beyancoback.beyanco.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.beyancoback.beyanco.model.Property;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    // Find all properties belonging to a user
    List<Property> findByUserId(Long userId);

    // Find one property by id and userId
    Optional<Property> findByIdAndUserId(Long id, Long userId);
    
    List<Property> findByChatHistoryId(Long chatHistoryId);
}
