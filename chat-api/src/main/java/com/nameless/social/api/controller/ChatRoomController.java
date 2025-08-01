package com.nameless.social.api.controller;

import com.nameless.social.api.dto.ChatRoomDto;
import com.nameless.social.api.model.ChatRoomModel;
import com.nameless.social.api.model.UserModel;
import com.nameless.social.api.response.CommonResponse;
import com.nameless.social.api.service.ChatRoomService;
import com.nameless.social.core.entity.ChatRoom;
import com.nameless.social.core.entity.ChatRoomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chatrooms")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @PostMapping
    public CommonResponse<ChatRoomModel> createChatRoom(@RequestBody ChatRoomDto chatRoomDto) {
        ChatRoom chatRoom = chatRoomService.createChatRoom(chatRoomDto.getName(), chatRoomDto.getParticipantIds());
        return CommonResponse.success(toModel(chatRoom));
    }

    @GetMapping("/{id}")
    public CommonResponse<ChatRoomModel> getChatRoomById(@PathVariable Long id) {
        ChatRoom chatRoom = chatRoomService.findById(id);
        return CommonResponse.success(toModel(chatRoom));
    }

    @GetMapping
    public CommonResponse<List<ChatRoomModel>> getAllChatRooms() {
        List<ChatRoom> chatRooms = chatRoomService.findAll();
        return CommonResponse.success(chatRooms.stream().map(this::toModel).collect(Collectors.toList()));
    }

    private ChatRoomModel toModel(ChatRoom chatRoom) {
        return ChatRoomModel.builder()
                .id(chatRoom.getId())
                .name(chatRoom.getName())
                .participants(chatRoom.getParticipants().stream().map(this::toUserModel).collect(Collectors.toList()))
                .createdAt(chatRoom.getCreatedAt())
                .updatedAt(chatRoom.getUpdatedAt())
                .build();
    }

    private UserModel toUserModel(ChatRoomUser chatRoomUser) {
        return UserModel.builder()
                .id(chatRoomUser.getUser().getId())
                .username(chatRoomUser.getUser().getUsername())
                .createdAt(chatRoomUser.getUser().getCreatedAt())
                .updatedAt(chatRoomUser.getUser().getUpdatedAt())
                .build();
    }
}
