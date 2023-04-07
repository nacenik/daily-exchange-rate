package net.oleksin;

import net.oleksin.api.ApiHttpClientRequester;
import net.oleksin.converter.impl.CsvRowsToStringConverter;
import net.oleksin.converter.impl.ObservationBySeriesDataToCsvRowsConverter;
import net.oleksin.parser.impl.CommandLineArgsParser;
import net.oleksin.parser.impl.JsonParser;
import net.oleksin.service.impl.CommandLineQueryBuilderService;
import net.oleksin.service.impl.CsvRowsDataPreparationService;
import net.oleksin.service.impl.WriterServiceImpl;

import java.net.http.HttpClient;

public class ExchangeRateApplication {
    public void run(String[] args, String fileName) {
        try {
            final var commandLineArgsParser = new CommandLineArgsParser();
            final var commandLineQueryService = new CommandLineQueryBuilderService(commandLineArgsParser);
            final var apiRequester = new ApiHttpClientRequester(HttpClient.newHttpClient(), commandLineQueryService);
            final var body = apiRequester.callApiByParams(args);

            final var jsonParser = new JsonParser();
            final var observationsBySeriesData = jsonParser.parse(body);

            final var csvRowsToStringConverter = new CsvRowsToStringConverter();
            final var observationBySeriesDataToCsvRowsConverter = new ObservationBySeriesDataToCsvRowsConverter();
            final var csvRowsDataPreparationService =
                    new CsvRowsDataPreparationService(csvRowsToStringConverter,
                            observationBySeriesDataToCsvRowsConverter);
            final var preparedData = csvRowsDataPreparationService.prepareData(observationsBySeriesData);

            final var writerService = new WriterServiceImpl();
            writerService.writeToFile(preparedData, fileName);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
