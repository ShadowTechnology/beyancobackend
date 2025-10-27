package com.beyancoback.beyanco.service;

import com.beyancoback.beyanco.model.ChatHistory;
import com.beyancoback.beyanco.model.User;
import com.beyancoback.beyanco.repo.ChatHistoryRepository;
import com.beyancoback.beyanco.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ChatHistoryService {

    @Autowired
    private ChatHistoryRepository chatRepo;

    @Autowired
    private UserRepo userRepo;

    public List<ChatHistory> getUserChats(String username) {
        User user = userRepo.findByUsername(username).orElse(null);
        if (user == null) return List.of();
        return chatRepo.findByUser(user);
    }

    public ChatHistory getChatById(Long id) {
        return chatRepo.findById(id).orElse(null);
    }

    public ChatHistory saveChat(ChatHistory chat) {
        return chatRepo.save(chat);
    }

    public void deleteChat(Long id) {
        chatRepo.deleteById(id);
    }
}
