package hr.javafx.webtrackly.app.files;

import hr.javafx.webtrackly.app.model.Entity;

import java.util.List;

public abstract class AbstractFileRepository<T extends Entity>{
    public abstract T findById(Long id);
    public abstract List<T> findAll();
    public abstract void save(List<T> entities);
    public abstract void save(T entity);


}

