package org.rso.naloga.zapiski.api.v1.resouces;

import comment.lib.Comment;
import comment.services.beans.CommentBean;

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
public class CommentResource {

    private Logger log = Logger.getLogger(CommentResource.class.getName());

    @Inject
    private CommentBean commentBean;

    @Context
    protected UriInfo uriInfo;

    @GET
    public Response getComments(){
        List<Comment> comments = commentBean.getAllComments();

        return Response.status(Response.Status.OK).entity(comments).build();
    }

    @GET
    @Path("{commentId}")
    public Response getCommentById(@PathParam("commentId") Long commentId){

        Comment comment = commentBean.getComment(commentId);

        if (comment == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return Response.status(Response.Status.OK).entity(comment).build();
    }

    @POST
    public Response createComment(Comment comment){

        if (comment.getUserId() == null || comment.getText() == null ||
                comment.getStarCount() == null) {

            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        comment = commentBean.createComment(comment);

        return Response.status(Response.Status.OK).entity(comment).build();
    }

}
