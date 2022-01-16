package comment.services.beans;

import comment.lib.Comment;
import comment.models.converters.CommentConverter;
import comment.models.entities.CommentEntity;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;
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

    @Timeout(value = 3, unit = ChronoUnit.SECONDS)
    @CircuitBreaker(requestVolumeThreshold = 2)
    @Fallback(fallbackMethod = "getCommentFallback")
    public Comment getComment(Long id){

        if(id==2022){
            try{
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e){
                System.out.println("Time error:\n" + e);
            }
        }

        CommentEntity CommentEn = em.find(CommentEntity.class, id);

        if (CommentEn == null){
            throw new NotFoundException();
        }

        Comment u = CommentConverter.toDto(CommentEn);

        return u;

    }

    public Comment getCommentFallback(Long id){
        Comment c = new Comment();
        c.setId((long)-1);
        return c;
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


    public boolean deleteComment(Long id){

        CommentEntity comment = em.find(CommentEntity.class, id);

        if (comment != null) {
            try {
                beginTx();
                em.remove(comment);
                commitTx();
            }
            catch (Exception e) {
                rollbackTx();
            }
        }
        else {
            return false;
        }

        return true;
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
