package hr.javafx.webtrackly.app.model;

public abstract class Entity {
    private Long id;

    public Entity() {}
    protected Entity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
