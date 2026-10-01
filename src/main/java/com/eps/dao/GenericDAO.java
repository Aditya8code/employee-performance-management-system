package com.eps.dao;

import java.util.List;

/**
 * Generic Data Access Object interface showcasing OOP Generics, Polymorphism,
 * and standard CRUD abstraction.
 *
 * @param <T>  The entity type
 * @param <ID> The entity primary key type
 */
public interface GenericDAO<T, ID> {

    T findById(ID id);

    List<T> findAll();

    T save(T entity);

    boolean update(T entity);

    boolean deleteById(ID id);
}
