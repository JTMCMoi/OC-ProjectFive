package com.openclassrooms.mdd_api.mapper;

import com.openclassrooms.mdd_api.dto.comment.CommentDto;
import com.openclassrooms.mdd_api.model.Comment;
import org.springframework.stereotype.Service;

@Service
public class CommentMapper {
    final private UserMapper userMapper;

    CommentMapper(final UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public CommentDto toDto(Comment comment) {
        return new CommentDto(comment.getComment_id(), comment.getContent(), this.userMapper.toUserDto(comment.getUser()), comment.getCreatedAt(), comment.getUpdatedAt());
    }
}
