package net.oleksin.factory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QueryBuilderServiceProviderTest {
    private static QueryBuilderServiceProvider queryBuilderServiceProvider;

    @BeforeAll
    static void beforeAll() {
        queryBuilderServiceProvider = new QueryBuilderServiceProvider();
    }

    @Test
    void getQueryBuilderServiceFactory_noArgs_returnSerialNameFactory() {
        final var queryBuilderServiceFactory = queryBuilderServiceProvider.getQueryBuilderServiceFactory(
                        new String[]{});
        checkResult(queryBuilderServiceFactory, SeriesNameQueryBuilderServiceFactory.class);
    }

    @Test
    void getQueryBuilderServiceFactory_oneDateArg_returnSerialNameFactory() {
        final var queryBuilderServiceFactory = queryBuilderServiceProvider.getQueryBuilderServiceFactory(
                        new String[]{"2020-01-01"});
        checkResult(queryBuilderServiceFactory, SeriesNameQueryBuilderServiceFactory.class);
    }

    @Test
    void getQueryBuilderServiceFactory_oneDateAndOneSerialNameArgs_returnSerialNameFactory() {
        final var queryBuilderServiceFactory = queryBuilderServiceProvider.getQueryBuilderServiceFactory(
                new String[]{"2020-01-01", "FXUSDEUR"});
        checkResult(queryBuilderServiceFactory, SeriesNameQueryBuilderServiceFactory.class);
    }

    @Test
    void getQueryBuilderServiceFactory_twoDateArgs_returnSerialNameFactory() {
        final var queryBuilderServiceFactory = queryBuilderServiceProvider.getQueryBuilderServiceFactory(
                        new String[]{"2020-01-01", "2020-01-01"});
        checkResult(queryBuilderServiceFactory, SeriesNameQueryBuilderServiceFactory.class);
    }

    @Test
    void getQueryBuilderServiceFactory_twoDateAndOneSerialNameArgs_returnSerialNameFactory() {
        final var queryBuilderServiceFactory = queryBuilderServiceProvider.getQueryBuilderServiceFactory(
                new String[]{"2020-01-01", "2020-01-01", "FXUSDEUR"});
        checkResult(queryBuilderServiceFactory, SeriesNameQueryBuilderServiceFactory.class);
    }

    @Test
    void getQueryBuilderServiceFactory_threeSerialNameArgs_returnSerialNameFactory() {
        final var queryBuilderServiceFactory = queryBuilderServiceProvider.getQueryBuilderServiceFactory(
                new String[]{"FXUSDEUR", "FXUSDCAD", "FXCADEUR"});
        checkResult(queryBuilderServiceFactory, SeriesNameQueryBuilderServiceFactory.class);
    }

    @Test
    void getQueryBuilderServiceFactory_oneSeriesGroupArg_returnSeriesGroupFactory() {
        final var queryBuilderServiceFactory = queryBuilderServiceProvider.getQueryBuilderServiceFactory(
                        new String[]{"FX_rates_daily"});
        checkResult(queryBuilderServiceFactory, SeriesGroupQueryBuilderServiceFactory.class);
    }

    @Test
    void getQueryBuilderServiceFactory_oneSeriesGroupAndOneDateArgs_returnSeriesGroupFactory() {
        final var queryBuilderServiceFactory = queryBuilderServiceProvider.getQueryBuilderServiceFactory(
                new String[]{"FX_rates_daily", "2020-01-01"});
        checkResult(queryBuilderServiceFactory, SeriesGroupQueryBuilderServiceFactory.class);
    }

    @Test
    void getQueryBuilderServiceFactory_oneSeriesGroupAndTwoDateArgs_returnSeriesGroupFactory() {
        final var queryBuilderServiceFactory = queryBuilderServiceProvider.getQueryBuilderServiceFactory(
                new String[]{"FX_rates_daily", "2020-01-01", "2020-01-01"});
        checkResult(queryBuilderServiceFactory, SeriesGroupQueryBuilderServiceFactory.class);
    }

    @Test
    void getQueryBuilderServiceFactory_twoSeriesGroupArgs_returnSeriesGroupFactory() {
        final var queryBuilderServiceFactory = queryBuilderServiceProvider.getQueryBuilderServiceFactory(
                new String[]{"FX_rates_daily", "FX_rates_monthly"});
        checkResult(queryBuilderServiceFactory, SeriesGroupQueryBuilderServiceFactory.class);
    }

    @Test
    void getQueryBuilderServiceFactory_oneSeriesGroupOneSeriesNameArgs_throwIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                queryBuilderServiceProvider.getQueryBuilderServiceFactory(
                        new String[]{"FX_rates_daily", "FXUSDEUR"}));
    }

    private static void checkResult(QueryBuilderServiceFactory queryBuilderServiceFactory, Class<?> clazz) {
        assertNotNull(queryBuilderServiceFactory);
        assertEquals(clazz, queryBuilderServiceFactory.getClass());
    }
}