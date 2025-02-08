package hr.javafx.webtrackly.app.db;

import hr.javafx.webtrackly.app.exception.RepositoryAccessException;
import hr.javafx.webtrackly.app.model.Entity;

import java.util.List;
import java.util.Set;

public abstract class AbstractDbRepository<T extends Entity> {
    public abstract List<T> findAll() throws RepositoryAccessException;
    public abstract void save(Set<T> entities) throws RepositoryAccessException;
    public abstract void save(T entity) throws RepositoryAccessException;


}
