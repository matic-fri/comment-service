package comment.models.entities;

import javax.persistence.*;

@Entity
@Table(name = "comment")
@NamedQueries(value =
        {
                @NamedQuery(name = "CommentEntity.getAll",
                        query = "SELECT im FROM CommentEntity im")
        })
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "file_id")
    private Long fileId;

    @Column(name = "stars")
    private Integer starCount;

    @Column(name = "text")
    private String text;


    // Getters
    public Long getId() {
        return this.id;
    }
    public Long getUserId() {
        return this.userId;
    }
    public Long getFileId() { return this.fileId;}
    public Integer getStarCount() {
        return this.starCount;
    }
    public String getText() {
        return this.text;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public void setFileId(Long fileId) { this.fileId = fileId;}
    public void setStarCount(Integer starCount) {
        this.starCount = starCount;
    }
    public void setText(String text) {
        this.text = text;
    }
}
