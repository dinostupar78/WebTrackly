package hr.javafx.webtrackly.app.db;

import hr.javafx.webtrackly.app.exception.RepositoryAccessException;
import hr.javafx.webtrackly.app.model.Entity;

import java.util.List;

public abstract class AbstractDbRepository<T extends Entity> {
    public abstract T findById(Long id) throws RepositoryAccessException;
    public abstract List<T> findAll() throws RepositoryAccessException;
    public abstract void save(List<T> entities) throws RepositoryAccessException;
    public abstract void save(T entity) throws RepositoryAccessException;


}
