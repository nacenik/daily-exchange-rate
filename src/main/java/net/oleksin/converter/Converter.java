package net.oleksin.converter;

public interface Converter<T, R> {
    R convert(T t);
}
