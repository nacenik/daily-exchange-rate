package net.oleksin;

import net.oleksin.api.ApiHttpClientRequester;
import net.oleksin.converter.CsvRowsToStringConverter;
import net.oleksin.converter.ListToCsvRowsConverter;
import net.oleksin.converter.ObservationBySeriesDataToCsvRowsConverter;
import net.oleksin.factory.QueryBuilderServiceProvider;
import net.oleksin.parser.JsonParser;
import net.oleksin.service.ApiAndFileDataAggregatorService;
import net.oleksin.service.ApiAndFileDataFusionService;
import net.oleksin.service.CsvRowsDataPreparationService;
import net.oleksin.service.DailyChangeCounterService;
import net.oleksin.service.FileReaderService;
import net.oleksin.service.FileWriterService;

import java.net.http.HttpClient;

public class ExchangeRateApplication {
    public void run(String[] args, String fileName) {
        try {
            final var serviceProvider = new QueryBuilderServiceProvider();
            final var commandLineBuilder = serviceProvider.getQueryBuilderServiceFactory(args)
                    .getCommandLineBuilder(args);

            final var apiRequester = new ApiHttpClientRequester(HttpClient.newHttpClient(),
                    commandLineBuilder);

            final var readerService = new FileReaderService();
            final var strings = readerService.readFromFile(fileName);

            final var body = apiRequester.callApiByParams(args);

            final var jsonParser = new JsonParser();
            final var observationsBySeriesData = jsonParser.parse(body);

            final var dataFusionService = new ApiAndFileDataFusionService();
            final var observationBySeriesDataToCsvRowsConverter = new ObservationBySeriesDataToCsvRowsConverter();
            final var aggregatorService = new ApiAndFileDataAggregatorService(observationBySeriesDataToCsvRowsConverter,
                    new ListToCsvRowsConverter(), dataFusionService);
            final var csvRows = aggregatorService.aggregate(strings, observationsBySeriesData);

            final var csvRowsToStringConverter = new CsvRowsToStringConverter();
            final var changeCounterService = new DailyChangeCounterService();
            final var csvRowsDataPreparationService =
                    new CsvRowsDataPreparationService(csvRowsToStringConverter, changeCounterService);
            final var preparedData = csvRowsDataPreparationService.prepareData(csvRows);

            final var writerService = new FileWriterService();
            writerService.writeToFile(preparedData, fileName);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
