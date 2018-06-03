package xyz.relentlesscrew.persistence.DAO;

import java.io.Serializable;
import java.util.List;

public interface GenericDAO<T, ID extends Serializable> {

    boolean add(T o);

    boolean remove(T o);

    void update(T o);

    T findById(ID id);

    List<T> findAll();

    List<T> findRange(int beginIndex, int endIndex);

    Long countRows();
}
