package com.nameless.social.api.service;

import com.nameless.social.api.exception.CustomException;
import com.nameless.social.api.exception.ErrorCode;
import com.nameless.social.api.repository.ChatRoomRepository;
import com.nameless.social.api.repository.ChatRoomUserRepository;
import com.nameless.social.api.repository.UserRepository;
import com.nameless.social.core.entity.ChatRoom;
import com.nameless.social.core.entity.ChatRoomUser;
import com.nameless.social.core.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;

    @Transactional
    public ChatRoom createChatRoom(String name, List<Long> participantIds) {
        ChatRoom chatRoom = new ChatRoom(name);
        chatRoomRepository.save(chatRoom);

        List<User> participants = userRepository.findAllById(participantIds);
        for (User user : participants) {
            ChatRoomUser chatRoomUser = new ChatRoomUser(user, chatRoom);
            chatRoomUserRepository.save(chatRoomUser);
        }

        return chatRoom;
    }

    public ChatRoom findById(Long id) {
        return chatRoomRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));
    }

    public List<ChatRoom> findAll() {
        return chatRoomRepository.findAll();
    }
}
