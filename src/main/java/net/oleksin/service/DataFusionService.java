package net.oleksin.service;

public interface DataFusionService<T extends Iterable<?>> {

    T fuseData(T firstData , T secondData);
}
