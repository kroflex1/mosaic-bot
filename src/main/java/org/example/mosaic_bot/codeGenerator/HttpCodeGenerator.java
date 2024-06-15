package org.example.mosaic_bot.codeGenerator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
        List<String> codes = getCodes(numberOfCodes, numberOfCodeReuse);
        File file = null;
        try {
            file = File.createTempFile("codes", ".txt");
            FileWriter writer = new FileWriter(file);
            writer.write(String.join("\n", codes));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

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
        JsonNode node;
        List<CodeInf> codesInf;
        try {
            node = OBJECT_MAPPER.readTree(response);
            codesInf = OBJECT_MAPPER.readValue(node.get("codes").toString(), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            return new ArrayList<>();
        }
        return codesInf.stream().map(CodeInf::id).collect(Collectors.toList());
    }

    private record CodesRequest(int count, int usage) {
    }

    private record CodeInf(String id, int usage) {

    }
}
