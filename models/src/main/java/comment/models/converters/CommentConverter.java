package comment.models.converters;


import comment.lib.Comment;
import comment.models.entities.CommentEntity;


public class CommentConverter {

    public static Comment toDto(CommentEntity entity) {
        Comment dto = new Comment();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setFileId(entity.getFileId());
        dto.setStarCount(entity.getStarCount());
        dto.setText(entity.getText());

        return dto;

    }

    public static CommentEntity toEntity(Comment dto){
        CommentEntity entity = new CommentEntity();
        entity.setId(dto.getId());
        entity.setUserId(dto.getUserId());
        entity.setFileId(dto.getFileId());
        entity.setStarCount(dto.getStarCount());
        entity.setText(dto.getText());

        return entity;

    }
}
