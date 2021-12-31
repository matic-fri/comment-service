package comment.services.beans;

import comment.lib.Comment;
import comment.models.converters.CommentConverter;
import comment.models.entities.CommentEntity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequestScoped
public class CommentBean {

    private Logger log = Logger.getLogger(CommentBean.class.getName());

    @Inject
    private EntityManager em;

    public List<Comment> getAllComments() {
        TypedQuery<CommentEntity> query = em.createNamedQuery(
                "CommentEntity.getAll", CommentEntity.class);

        List<CommentEntity> resultList = query.getResultList();

        return resultList.stream().map(CommentConverter::toDto).collect(Collectors.toList());

    }

    public Comment getComment(Long id){
        CommentEntity CommentEn = em.find(CommentEntity.class, id);

        if (CommentEn == null){
            throw new NotFoundException();
        }

        Comment u = CommentConverter.toDto(CommentEn);

        return u;

    }

    public Comment createComment(Comment user){

        CommentEntity CommentEn = CommentConverter.toEntity(user);

        try{
            beginTx();
            em.persist(CommentEn);
            commitTx();
        }
        catch (Exception e){
            rollbackTx();
        }

        if (CommentEn.getId() == null){
            throw new RuntimeException("Entity comment was not persisted");
        }

        return CommentConverter.toDto(CommentEn);

    }

    public Comment updateComment(Long id, Comment comment) {

        CommentEntity CommentEn_old = em.find(CommentEntity.class, id);

        if (CommentEn_old == null) {
            return null;
        }

        CommentEntity CommentEn_new = CommentConverter.toEntity(comment);

        try{
            beginTx();
            CommentEn_new.setId(CommentEn_old.getId());
            CommentEn_new = em.merge(CommentEn_new);
            commitTx();
        }
        catch (Exception e){
            rollbackTx();
        }

        return CommentConverter.toDto(CommentEn_new);
    }



    private void beginTx(){
        if(!em.getTransaction().isActive()){
            em.getTransaction().begin();
        }
    }

    private void commitTx(){
        if (em.getTransaction().isActive()){
            em.getTransaction().commit();
        }
    }

    private void rollbackTx(){
        if(em.getTransaction().isActive()){
            em.getTransaction().rollback();
        }
    }

}
