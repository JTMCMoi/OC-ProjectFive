package com.openclassrooms.mdd_api.service.interfaces;

import com.openclassrooms.mdd_api.dto.comment.CommentDto;
import com.openclassrooms.mdd_api.dto.comment.CreateCommentRequest;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.List;

public interface ICommentService {
    List<CommentDto> addPostComment(final JwtAuthenticationToken token, final CreateCommentRequest comment);
}
