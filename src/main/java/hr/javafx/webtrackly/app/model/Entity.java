package hr.javafx.webtrackly.app.model;

/**
 * Apstraktna klasa koja predstavlja entitet u aplikaciji WebTrackly.
 * Sadrži jedinstveni identifikator entiteta.
 */

public abstract class Entity {
    private Long id;

    protected Entity() {}

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
