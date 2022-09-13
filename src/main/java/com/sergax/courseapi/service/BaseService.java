package com.sergax.courseapi.service;

import java.util.List;

public interface BaseService<V, T> {
    List<V> findAll();
    V findById(T id);
    V save(V v);
    V update(T id, V v);
    void deleteById(T id);
}
