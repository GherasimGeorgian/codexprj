package chat.persistance;

import chat.model.Identifiable;

public interface ICrudRepository<ID, E extends Identifiable<ID>> {
    E save(E e);
    void delete(ID id);
    E findOne(ID id);
    void update(ID id, E e);
    Iterable<E> getAll();
}
