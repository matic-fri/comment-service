package org.rso.naloga.zapiski.api.v1.resouces;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import comment.lib.Comment;
import comment.lib.UpdateComment;
import comment.services.beans.CommentBean;
import comment.services.clients.WordFilter;
import org.eclipse.microprofile.metrics.annotation.*;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
@Path("/comments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@CrossOrigin(supportedMethods = "GET, POST, PUT, DELETE, OPTIONS")
public class CommentResource {

    private Logger log = Logger.getLogger(CommentResource.class.getName());

    @Inject
    private CommentBean commentBean;

    @Inject
    private WordFilter wordFilter;

    @Context
    protected UriInfo uriInfo;



    @Operation(description = "Get all comments.", summary = "Get comments.")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "All comments",
                    content = @Content(schema = @Schema(implementation = Comment.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Comments")}
            )})
    @SimplyTimed
    @GET
    public Response getComments(){
        List<Comment> comments = commentBean.getAllComments();


        return Response.status(Response.Status.OK).entity(comments).build();
    }


    @Operation(description = "Get specified comment.", summary = "Get comment.")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Specified comment",
                    content = @Content(schema = @Schema(implementation = Comment.class, type = SchemaType.OBJECT)),
                    headers = {@Header(name = "X-Total-Count", description = "Comment")}
            )})
    @GET
    @Counted
    @Path("{commentId}")
    public Response getCommentById(@PathParam("commentId") Long commentId){

        Comment comment = commentBean.getComment(commentId);

        if (comment == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if(comment.getId()==-1){
            return Response.status(500, "Fallback. Comment doesn't exist or circuit open").build();
        }


        return Response.status(Response.Status.OK).entity(comment).build();
    }


    @Operation(description = "Create new comment", summary = "Create comment.")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "New comment",
                    content = @Content(schema = @Schema(implementation = Comment.class, type = SchemaType.OBJECT)),
                    headers = {@Header(name = "X-Total-Count", description = "Comment")}
            )})
    @POST
    @Counted
    public Response createComment(Comment comment){

        if (comment.getUserId() == null || comment.getText() == null ||
                comment.getStarCount() == null) {

            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if(wordFilter.hasBadWords(comment.getText())){
            return Response.status(406, "Comment has bad words").build();
        }


        comment = commentBean.createComment(comment);

        return Response.status(Response.Status.OK).entity(comment).build();
    }

    @Operation(description = "Update a comments stars and text.", summary = "Update comment.")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Updated a comments stars and text.",
                    content = @Content(schema = @Schema(implementation = UpdateComment.class, type = SchemaType.OBJECT)),
                    headers = {@Header(name = "X-Total-Count", description = "Comment")}
            )})
    @PUT
    @Counted
    @Path("{commentId}")
    public Response updateComment(@PathParam("commentId") long commentId, UpdateComment newComment){

        Comment comment = commentBean.getComment(commentId);

        if(comment==null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if(wordFilter.hasBadWords(newComment.getNewText())){
            return Response.status(406, "New comment has bad words").build();
        }

        comment.setStarCount(newComment.getNewStars());

        comment.setText(newComment.getNewText());

        comment = commentBean.updateComment(commentId, comment);

        return Response.status(Response.Status.OK).entity(comment).build();


    }

    @Operation(description = "Delete a comment with given id.", summary = "Delete a comment.")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Delete a comment with given id.",
                    content = @Content(schema = @Schema(implementation = Comment.class, type = SchemaType.OBJECT)),
                    headers = {@Header(name = "X-Total-Count", description = "Comment")}
            )})
    @DELETE
    @Counted
    @Path("{commentId}")
    public Response deleteComment(@PathParam("commentId") long commentId){

        boolean deleted = commentBean.deleteComment(commentId);

        if (deleted) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

    }

}
