package hr.javafx.webtrackly.app.db;
import hr.javafx.webtrackly.app.exception.RepositoryException;
import hr.javafx.webtrackly.app.model.Entity;
import java.util.List;

/**
 * Apstraktna klasa koja definira osnovne operacije za rad s bazom podataka.
 * Služi kao osnova za specifične repozitorije koji upravljaju entitetima.
 *
 * @param <T> Tip entiteta koji se upravlja u repozitoriju.
 */

public abstract class AbstractDbRepository<T extends Entity> {
    public abstract T findById(Long id) throws RepositoryException;
    public abstract List<T> findAll() throws RepositoryException;
    public abstract void save(List<T> entities) throws RepositoryException;
    public abstract void save(T entity) throws RepositoryException;


}
