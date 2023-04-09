package net.oleksin.factory;

import net.oleksin.parser.impl.SeriesNameCommandLineArgsParser;
import net.oleksin.service.QueryBuilderService;
import net.oleksin.service.impl.SeriesNameCommandLineQueryBuilderService;

public class SeriesNameQueryBuilderServiceFactory implements QueryBuilderServiceFactory {
    @Override
    public QueryBuilderService getCommandLineBuilder(String[] args) {
        return new SeriesNameCommandLineQueryBuilderService(new SeriesNameCommandLineArgsParser());
    }
}
