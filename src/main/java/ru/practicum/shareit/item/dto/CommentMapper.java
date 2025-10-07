package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Comment;

@Component
public class CommentMapper {

    public static Comment fromCommentDto(CommentDto commentDto) {
        return new Comment(commentDto.getText());
    }

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(), comment.getAuthor().getName(), comment.getItem().getId(),
                comment.getCreated());
    }
}
