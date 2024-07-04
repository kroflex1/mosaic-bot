package org.example.mosaic_bot;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.example.mosaic_bot.web.MosaicWeb;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

@WireMockTest(httpPort = 8080)
public class HttpCodeGeneratorTest {
    private static final MosaicWeb MOSAIC_WEB  = new MosaicWeb("http://localhost:8080", "admin", "admin");

    @Test
    public void testGetInformationAboutRepository(WireMockRuntimeInfo wmRuntimeInfo) {
        WireMock wireMock = wmRuntimeInfo.getWireMock();
        wireMock.loadMappingsFrom("src/test/java/resources/wiremock/");

        List<String> expected = List.of("b1", "b2", "b3");
        List<String> actual = MOSAIC_WEB.getCodes(3, 1);
        Assertions.assertIterableEquals(expected, actual);
    }
}
