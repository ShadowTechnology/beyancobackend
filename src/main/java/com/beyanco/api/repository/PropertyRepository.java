package com.beyanco.api.repository;

import com.beyanco.api.model.Property;
import com.beyanco.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findByUser(User user);

    List<Property> findByUserOrderByCreatedAtDesc(User user);
}
