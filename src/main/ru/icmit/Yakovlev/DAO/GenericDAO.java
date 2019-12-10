package DAO;
import java.util.List;
import java.util.Optional;

public interface GenericDAO<T> {

    Optional<T> find(T model) throws ClassNotFoundException;
    void save(T model) throws ClassNotFoundException;
    void update(T model) throws ClassNotFoundException;
    void delete(T model) throws ClassNotFoundException;
    List<T> findAll() throws ClassNotFoundException;


}
