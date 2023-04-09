package net.oleksin;

import net.oleksin.api.ApiHttpClientRequester;
import net.oleksin.converter.impl.CsvRowsToStringConverter;
import net.oleksin.converter.impl.ListToCsvRowsConverter;
import net.oleksin.converter.impl.ObservationBySeriesDataToCsvRowsConverter;
import net.oleksin.factory.QueryBuilderServiceProvider;
import net.oleksin.model.CommandLineArgs;
import net.oleksin.parser.Parser;
import net.oleksin.parser.impl.SeriesNameCommandLineArgsParser;
import net.oleksin.parser.impl.SeriesGroupCommandLineArgsParser;
import net.oleksin.parser.impl.JsonParser;
import net.oleksin.service.QueryBuilderService;
import net.oleksin.service.impl.AggregatorServiceImpl;
import net.oleksin.service.impl.ChangeCounterServiceImpl;
import net.oleksin.service.impl.SeriesNameCommandLineQueryBuilderService;
import net.oleksin.service.impl.CsvRowsDataPreparationService;
import net.oleksin.service.impl.DataFusionServiceImpl;
import net.oleksin.service.impl.ReaderServiceImpl;
import net.oleksin.service.impl.WriterServiceImpl;

import java.net.http.HttpClient;

public class ExchangeRateApplication {
    public void run(String[] args, String fileName) {
        try {
            final var serviceProvider = new QueryBuilderServiceProvider();
            final var commandLineBuilder = serviceProvider.getQueryBuilderServiceFactory(args)
                    .getCommandLineBuilder(args);

            final var apiRequester = new ApiHttpClientRequester(HttpClient.newHttpClient(),
                    commandLineBuilder);

            final var readerService = new ReaderServiceImpl();
            final var strings = readerService.readFromFile(fileName);

            final var body = apiRequester.callApiByParams(args);

            final var jsonParser = new JsonParser();
            final var observationsBySeriesData = jsonParser.parse(body);

            final var dataFusionService = new DataFusionServiceImpl();
            final var observationBySeriesDataToCsvRowsConverter = new ObservationBySeriesDataToCsvRowsConverter();
            final var aggregatorService = new AggregatorServiceImpl(observationBySeriesDataToCsvRowsConverter,
                    new ListToCsvRowsConverter(), dataFusionService);
            final var csvRows = aggregatorService.aggregate(strings, observationsBySeriesData);

            final var csvRowsToStringConverter = new CsvRowsToStringConverter();
            final var changeCounterService = new ChangeCounterServiceImpl();
            final var csvRowsDataPreparationService =
                    new CsvRowsDataPreparationService(csvRowsToStringConverter, changeCounterService);
            final var preparedData = csvRowsDataPreparationService.prepareData(csvRows);

            final var writerService = new WriterServiceImpl();
            writerService.writeToFile(preparedData, fileName);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
