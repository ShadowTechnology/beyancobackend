package com.beyancoback.beyanco.controller;

import com.beyancoback.beyanco.model.ChatHistory;
import com.beyancoback.beyanco.model.User;
import com.beyancoback.beyanco.service.ChatHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.beyancoback.beyanco.security.services.UserDetailsImpl;


import java.util.List;

@RestController
@RequestMapping("/api/chats")
@CrossOrigin(origins = "http://localhost:4200")
public class ChatHistoryController {

    @Autowired
    private ChatHistoryService chatService;

    @GetMapping("/getUserChats")
    public List<ChatHistory> getUserChats() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        String username = userDetails.getUsername();
        return chatService.getUserChats(username);
    }

    @PostMapping("/createChat")
    public ChatHistory createChat(@RequestBody ChatHistory chat) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        String username = userDetails.getUsername();

        // attach current user
        User user = new User();
        user.setId(userDetails.getId());
        chat.setUser(user);

        return chatService.saveChat(chat);
    }

    @PutMapping("updateChat/{id}")
    public ChatHistory updateChat(@PathVariable Long id, @RequestBody ChatHistory chat) {
        ChatHistory existing = chatService.getChatById(id);
        if (existing == null) return null;

        existing.setTitle(chat.getTitle());
        existing.setMessages(chat.getMessages());
        return chatService.saveChat(existing);
    }

    @DeleteMapping("deleteChat/{id}")
    public void deleteChat(@PathVariable Long id) {
        chatService.deleteChat(id);
    }
}
