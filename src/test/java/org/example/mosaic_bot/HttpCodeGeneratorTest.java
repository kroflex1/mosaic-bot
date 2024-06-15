package org.example.mosaic_bot;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.example.mosaic_bot.codeGenerator.HttpCodeGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.repository.core.RepositoryInformation;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WireMockTest(httpPort = 8080)
public class HttpCodeGeneratorTest {
    private static final HttpCodeGenerator HTTP_CODE_GENERATOR = new HttpCodeGenerator("http://localhost:8080");

    @Test
    public void testGetInformationAboutRepository(WireMockRuntimeInfo wmRuntimeInfo) {
        WireMock wireMock = wmRuntimeInfo.getWireMock();
        wireMock.loadMappingsFrom("src/test/java/resources/wiremock/");

        List<String> expected = List.of("b1", "b2", "b3");
        List<String> actual = HTTP_CODE_GENERATOR.getCodes(3, 1);
        Assertions.assertIterableEquals(expected, actual);
    }
}
