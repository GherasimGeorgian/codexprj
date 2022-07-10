package com.project.repository.interfaces;

import java.util.List;

public interface IRepository<ID, T> {

    T save(T entity);
    T findOne(ID id);
    List<T> findAll();
    T update(T entity,T newEntity);
    T delete(ID id);
}
