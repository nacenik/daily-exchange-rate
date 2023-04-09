package net.oleksin.api;

import lombok.RequiredArgsConstructor;
import net.oleksin.exception.HttpResponseException;
import net.oleksin.service.QueryBuilderService;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RequiredArgsConstructor
public class ApiHttpClientRequester implements ApiRequester {
    private static final int STATUS_OK = 200;
    private final HttpClient httpClient;
    private final QueryBuilderService queryService;

    @Override
    public String callApiByParams(String[] args) {
        var request = HttpRequest.newBuilder(
                        URI.create(queryService.buildQuery(args)))
                .header("accept", "application/json")
                .build();
        try {
            final var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            final var statusCode = response.statusCode();
            if (statusCode == STATUS_OK) {
                return response.body();
            } else {
                throw new HttpResponseException(String
                        .format("Failed to fetch data from the Bank of Canada API.%sStatus code: %d%sBody: %s",
                                System.lineSeparator(), statusCode,
                                System.lineSeparator(), response.body()));
            }
        } catch (IOException | InterruptedException e) {
            throw new HttpResponseException(e.getMessage());
        }
    }
}
