package net.oleksin.parser;

public interface Parser<T, R> {
    R parse(T t);
}
