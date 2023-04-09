package net.oleksin;

import net.oleksin.api.ApiHttpClientRequester;
import net.oleksin.converter.impl.CsvRowsToStringConverter;
import net.oleksin.converter.impl.ListToCsvRowsConverter;
import net.oleksin.converter.impl.ObservationBySeriesDataToCsvRowsConverter;
import net.oleksin.model.CsvRow;
import net.oleksin.parser.impl.CommandLineArgsParser;
import net.oleksin.parser.impl.JsonParser;
import net.oleksin.service.impl.AggregatorServiceImpl;
import net.oleksin.service.impl.ChangeCounterServiceImpl;
import net.oleksin.service.impl.CommandLineQueryBuilderService;
import net.oleksin.service.impl.CsvRowsDataPreparationService;
import net.oleksin.service.impl.DataFusionServiceImpl;
import net.oleksin.service.impl.ObservationsBySeriesDataPreparationService;
import net.oleksin.service.impl.ReaderServiceImpl;
import net.oleksin.service.impl.WriterServiceImpl;

import java.net.http.HttpClient;
import java.util.List;

public class ExchangeRateApplication {
    public void run(String[] args, String fileName) {
        try {
            final var readerService = new ReaderServiceImpl();
            final var strings = readerService.readFromFile(fileName);

            final var commandLineArgsParser = new CommandLineArgsParser();
            final var commandLineQueryService = new CommandLineQueryBuilderService(commandLineArgsParser);
            final var apiRequester = new ApiHttpClientRequester(HttpClient.newHttpClient(), commandLineQueryService);
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
