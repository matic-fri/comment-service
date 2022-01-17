package org.rso.naloga.zapiski.graphql;

import com.kumuluz.ee.graphql.annotations.GraphQLClass;
import com.kumuluz.ee.graphql.classes.Filter;
import com.kumuluz.ee.graphql.classes.Pagination;
import com.kumuluz.ee.graphql.classes.PaginationWrapper;
import com.kumuluz.ee.graphql.classes.Sort;
import com.kumuluz.ee.graphql.utils.GraphQLUtils;
import comment.lib.Comment;
import comment.services.beans.CommentBean;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLQuery;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@GraphQLClass
@ApplicationScoped
public class CommentQueries {

    @Inject
    private CommentBean commentBean;

    @GraphQLQuery
    public Comment getComment(@GraphQLArgument(name = "id") Long id){
        return commentBean.getComment(id);
    }

    @GraphQLQuery
    public PaginationWrapper<Comment> getAllComments(@GraphQLArgument(name = "pagination") Pagination pagination,
                                                     @GraphQLArgument(name = "sort") Sort sort,
                                                     @GraphQLArgument(name = "filter") Filter filter){

        return GraphQLUtils.process(commentBean.getAllComments(), pagination, sort, filter);
    }
}
