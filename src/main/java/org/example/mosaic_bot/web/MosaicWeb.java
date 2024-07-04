package org.example.mosaic_bot.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.mosaic_bot.exceptions.CantConnectToServerException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class MosaicWeb {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String CODES_PATH = "api/v1/code";
    private static final String LOGIN_PATH = "api/v1/auth/login";
    private final String baseUrl;
    private final String adminLogin;
    private final String adminPassword;

    public MosaicWeb(String baseUrl, String adminLogin, String adminPassword) {
        this.baseUrl = baseUrl;
        this.adminLogin = adminLogin;
        this.adminPassword = adminPassword;
    }

    public File generateFileWithCodes(int numberOfCodes, int numberOfCodeReuse) throws CantConnectToServerException {
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
        WebClient webClient = org.springframework.web.reactive.function.client.WebClient
                .builder()
                .baseUrl(baseUrl)
                .build();
        CodesRequest body = new CodesRequest(numberOfCodes, numberOfCodeReuse);
        String response = webClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path(CODES_PATH)
                        .build())
                .bodyValue(body)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> Mono.empty())
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

    public Optional<String> getAccessToken(String adminLogin, String adminPassword) {
        Optional<UserLoginInf> userLoginInf = loginToService(adminLogin, adminPassword);
        return userLoginInf.map(UserLoginInf::getAccess_token);
    }

    private Optional<UserLoginInf> loginToService(String adminLogin, String adminPassword) {
        WebClient webClient = org.springframework.web.reactive.function.client.WebClient
                .builder()
                .baseUrl(baseUrl)
                .build();
        ResponseEntity<UserLoginInf> userLoginInf = webClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path(LOGIN_PATH)
                        .build())
                .bodyValue(new LoginRequest(adminLogin, adminPassword))
                .retrieve()
                .toEntity(UserLoginInf.class)
                .block();
        if (userLoginInf.getStatusCode().isError()) {
            return Optional.empty();
        }
        return Optional.of(userLoginInf.getBody());
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    private class UserLoginInf {
        private String access_token;
        private String refresh_token;
        private UserInf userInf;

        private record UserInf(UUID id, String name, String email, String role, String registeredAt) {

        }
    }

    private record LoginRequest(String login, String password) {

    }

    private record CodeInf(String id, int usage) {

    }

    private record CodesRequest(int count, int usage) {
    }
}
