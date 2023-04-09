package net.oleksin.factory;

import net.oleksin.parser.SeriesGroupCommandLineArgsParser;
import net.oleksin.service.QueryBuilderService;
import net.oleksin.service.SeriesGroupCommandLineQueryBuilderService;

public class SeriesGroupQueryBuilderServiceFactory implements QueryBuilderServiceFactory {
    @Override
    public QueryBuilderService getCommandLineBuilder(String[] args) {
        return new SeriesGroupCommandLineQueryBuilderService(new SeriesGroupCommandLineArgsParser());
    }
}
