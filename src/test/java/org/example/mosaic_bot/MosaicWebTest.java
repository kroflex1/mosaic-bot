package org.example.mosaic_bot;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.example.mosaic_bot.web.MosaicWeb;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

@WireMockTest(httpPort = 8080)
public class MosaicWebTest {
    private static final MosaicWeb MOSAIC_WEB = new MosaicWeb("http://localhost:8080", "admin", "admin");

    @Test
    public void testGetCodes(WireMockRuntimeInfo wmRuntimeInfo) {
        WireMock wireMock = wmRuntimeInfo.getWireMock();
        wireMock.loadMappingsFrom("src/test/java/resources/wiremock/");

        List<String> expected = List.of("b1", "b2", "b3");
        List<String> actual = MOSAIC_WEB.getCodes(3, 1);

        Assertions.assertIterableEquals(expected, actual);
    }

    @Test
    public void testGetToken(WireMockRuntimeInfo wmRuntimeInfo) {
        WireMock wireMock = wmRuntimeInfo.getWireMock();
        wireMock.loadMappingsFrom("src/test/java/resources/wiremock/");

        String expectedToken = "eyJhbGciOiJIUzI1NiIsImtpZCI6ImFkbWluIiwidHlwIjoiSldUIn0.eyJpZCI6Ijk5Y2M5MWY1LWI3MzctNDc1Yy1hNWIyLThhYjAxYTc2MzViMiIsIm5hbWUiOiJhZG1pbiIsImVtYWlsIjoiYWRtaW5AYWRtaW4uY29tIiwicm9sZSI6ImFkbWluIiwidG9rZW5UeXBlIjoiYWNjZXNzIiwic3ViIjoiOTljYzkxZjUtYjczNy00NzVjLWE1YjItOGFiMDFhNzYzNWIyIiwiZXhwIjoxNzIwMzQyODM3LCJuYmYiOjE3MjAzNDE5MzcsImlhdCI6MTcyMDM0MTkzNywianRpIjoiMDE0ZDQ4MWYtYjE5NC00ODNlLTkxMDQtYjI3NzdlMzU0ZDkzIn0.a5_9NCs1sDVqH2FBO3ztwgVe1UTqpTw3ew76PmGC17Q";
        Optional<String> actualToken = MOSAIC_WEB.getAccessToken("admin", "admin");

        Assertions.assertEquals(expectedToken, actualToken.get());
    }
}
