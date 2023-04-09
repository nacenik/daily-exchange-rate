package net.oleksin.factory;

import net.oleksin.parser.SeriesNameCommandLineArgsParser;
import net.oleksin.service.QueryBuilderService;
import net.oleksin.service.SeriesNameCommandLineQueryBuilderService;

public class SeriesNameQueryBuilderServiceFactory implements QueryBuilderServiceFactory {
    @Override
    public QueryBuilderService getCommandLineBuilder(String[] args) {
        return new SeriesNameCommandLineQueryBuilderService(new SeriesNameCommandLineArgsParser());
    }
}
