package comment.lib;

public class Comment {

    private Long id;
    private Long userId;
    private Long fileId;
    private Integer starCount;
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
