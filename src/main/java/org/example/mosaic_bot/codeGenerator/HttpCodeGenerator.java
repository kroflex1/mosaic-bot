package org.example.mosaic_bot.codeGenerator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpCodeGenerator implements CodeGenerator {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String PATH_TO_GET_CODES = "api/v1/code";
    private final WebClient webClient;

    public HttpCodeGenerator(String baseUrl) {
        this.webClient = org.springframework.web.reactive.function.client.WebClient
                .builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Override
    public File generateFileWithCodes(int numberOfCodes, int numberOfCodeReuse) {
        return null;
    }

    @SneakyThrows
    public List<String> getCodes(int numberOfCodes, int numberOfCodeReuse) {
        CodesRequest body = new CodesRequest(numberOfCodes, numberOfCodeReuse);
        String response = webClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH_TO_GET_CODES)
                        .build())
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        JsonNode node = OBJECT_MAPPER.readTree(response);
        var x = node.get("codes").toString();
        List<CodeInf> codesInf = OBJECT_MAPPER.readValue(x, new TypeReference<>() {});
        return codesInf.stream().map(CodeInf::id).collect(Collectors.toList());
    }

    private record CodesRequest(int count, int usage) {
    }

    private record CodeInf(String id, int usage){

    }
}
