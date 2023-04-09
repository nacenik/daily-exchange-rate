package net.oleksin.service;

public interface AggregatorService<T, R, E> {
    E aggregate(T t, R r);
}
