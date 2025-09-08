package com.solvd.uber.dao;

import java.util.List;

public interface IBaseDAO<T> {
    T getById(long id);
    List<T> getAll();
    void insert(T entity);
    void update(T entity);
    void delete(long id);
}
