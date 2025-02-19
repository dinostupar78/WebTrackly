package hr.javafx.webtrackly.app.db;

import hr.javafx.webtrackly.app.exception.RepositoryException;
import hr.javafx.webtrackly.app.model.Entity;

import java.util.List;

public abstract class AbstractDbRepository<T extends Entity> {
    public abstract T findById(Long id) throws RepositoryException;
    public abstract List<T> findAll() throws RepositoryException;
    public abstract void save(List<T> entities) throws RepositoryException;
    public abstract void save(T entity) throws RepositoryException;


}
