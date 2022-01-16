package org.rso.naloga.zapiski.graphql;

import com.kumuluz.ee.graphql.annotations.GraphQLClass;
import comment.lib.Comment;
import comment.lib.UpdateComment;
import comment.services.beans.CommentBean;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@GraphQLClass
@ApplicationScoped
public class CommentMutations {

    @Inject
    private CommentBean commentBean;

    @GraphQLMutation
    public Comment addComment(@GraphQLArgument(name = "comment") Comment comment){
        commentBean.createComment(comment);
        return comment;
    }

    @GraphQLMutation
    public Comment changeComment(@GraphQLArgument(name = "id") long commentId,
            @GraphQLArgument(name = "updatedComment") UpdateComment uc){
        Comment c = commentBean.getComment(commentId);
        c.setText(uc.getNewText());
        c.setStarCount(uc.getNewStars());
        commentBean.updateComment(commentId, c);
        return c;
    }

    @GraphQLMutation
    public DeleteResponse deleteComment(@GraphQLArgument(name = "id") long commentId){
        return new DeleteResponse(commentBean.deleteComment(commentId));
    }
}
