package com.beyancoback.beyanco.repo;

import com.beyancoback.beyanco.model.ChatHistory;
import com.beyancoback.beyanco.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long> {
    List<ChatHistory> findByUser(User user);
}
