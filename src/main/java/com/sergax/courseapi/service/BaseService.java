package com.sergax.courseapi.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BaseService<V, T> {
    List<V> findAll();
    V findById(T id);
    V create(V v);
    V update(T id, V v);
    void deleteById(T id);
}
