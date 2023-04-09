package net.oleksin.factory;

import net.oleksin.service.QueryBuilderService;

public interface QueryBuilderServiceFactory {
    QueryBuilderService getCommandLineBuilder(String[] args);
}
