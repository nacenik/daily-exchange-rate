package net.oleksin.api;

import lombok.SneakyThrows;
import net.oleksin.exception.HttpResponseException;
import net.oleksin.service.QueryBuilderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ApiHttpClientRequesterTest {
    private static final String TEST_QUERY = "http://www.test.com";
    private static ApiHttpClientRequester apiHttpClientRequester;
    private static HttpClient mockHttpClient;
    private static QueryBuilderService mockQueryBuilderService;
    private static HttpResponse mockHttpResponse;


    @BeforeAll
    static void beforeAll() {
        mockHttpClient = Mockito.mock(HttpClient.class);
        mockQueryBuilderService = Mockito.mock(QueryBuilderService.class);
        apiHttpClientRequester = new ApiHttpClientRequester(mockHttpClient, mockQueryBuilderService);
        mockHttpResponse = Mockito.mock(HttpResponse.class);
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(mockHttpClient, mockQueryBuilderService, mockHttpResponse);
    }

    @Test
    @SneakyThrows
    void callApiByParams_validQuery_ok() {
        Mockito.when(mockQueryBuilderService.buildQuery(Mockito.any())).thenReturn(TEST_QUERY);
        Mockito.when(mockHttpClient.send(Mockito.any(), Mockito.any()))
                .thenReturn(mockHttpResponse);
        Mockito.when(mockHttpResponse.statusCode()).thenReturn(200);

        apiHttpClientRequester.callApiByParams(Mockito.any());
        Mockito.verify(mockHttpClient).send(Mockito.any(), Mockito.any());
        Mockito.verify(mockQueryBuilderService).buildQuery(Mockito.any());
        Mockito.verify(mockHttpResponse).statusCode();
        Mockito.verify(mockHttpResponse).body();
    }

    @Test
    @SneakyThrows
    void callApiByParams_4xxStatusCode_throwHttpResponseException() {
        Mockito.when(mockQueryBuilderService.buildQuery(Mockito.any())).thenReturn(TEST_QUERY);
        Mockito.when(mockHttpClient.send(Mockito.any(), Mockito.any()))
                .thenReturn(mockHttpResponse);
        Mockito.when(mockHttpResponse.statusCode()).thenReturn(404);

        assertThrows(HttpResponseException.class, () ->
                apiHttpClientRequester.callApiByParams(Mockito.any()));

        Mockito.verify(mockHttpClient).send(Mockito.any(), Mockito.any());
        Mockito.verify(mockQueryBuilderService).buildQuery(Mockito.any());
        Mockito.verify(mockHttpResponse).statusCode();
        Mockito.verify(mockHttpResponse).body();
    }

    @Test
    @SneakyThrows
    void callApiByParams_3xxStatusCode_throwHttpResponseException() {
        Mockito.when(mockQueryBuilderService.buildQuery(Mockito.any())).thenReturn(TEST_QUERY);
        Mockito.when(mockHttpClient.send(Mockito.any(), Mockito.any()))
                .thenReturn(mockHttpResponse);
        Mockito.when(mockHttpResponse.statusCode()).thenReturn(302);

        assertThrows(HttpResponseException.class, () ->
                apiHttpClientRequester.callApiByParams(Mockito.any()));

        Mockito.verify(mockHttpClient).send(Mockito.any(), Mockito.any());
        Mockito.verify(mockQueryBuilderService).buildQuery(Mockito.any());
        Mockito.verify(mockHttpResponse).statusCode();
        Mockito.verify(mockHttpResponse).body();
    }

    @Test
    @SneakyThrows
    void callApiByParams_httpClientInnerException_throwHttpResponseException() {
        Mockito.when(mockQueryBuilderService.buildQuery(Mockito.any())).thenReturn(TEST_QUERY);
        Mockito.when(mockHttpClient.send(Mockito.any(), Mockito.any()))
                .thenThrow(new IOException("Test exception"));
        Mockito.when(mockHttpResponse.statusCode()).thenReturn(302);

        assertThrows(HttpResponseException.class, () ->
                apiHttpClientRequester.callApiByParams(Mockito.any()));

        Mockito.verify(mockHttpClient).send(Mockito.any(), Mockito.any());
        Mockito.verify(mockQueryBuilderService).buildQuery(Mockito.any());
        Mockito.verify(mockHttpResponse, Mockito.never()).statusCode();
        Mockito.verify(mockHttpResponse, Mockito.never()).body();
    }

    @Test
    @SneakyThrows
    void callApiByParams_badQuery_throwIllegalArgumentException() {
        Mockito.when(mockQueryBuilderService.buildQuery(Mockito.any())).thenReturn("test");
        Mockito.when(mockHttpResponse.statusCode()).thenReturn(302);

        assertThrows(IllegalArgumentException.class, () ->
                apiHttpClientRequester.callApiByParams(Mockito.any()));

        Mockito.verify(mockHttpClient, Mockito.never()).send(Mockito.any(), Mockito.any());
        Mockito.verify(mockQueryBuilderService).buildQuery(Mockito.any());
        Mockito.verify(mockHttpResponse, Mockito.never()).statusCode();
        Mockito.verify(mockHttpResponse, Mockito.never()).body();
    }
}