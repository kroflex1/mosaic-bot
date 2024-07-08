package org.example.mosaic_bot.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.resolver.DefaultAddressResolverGroup;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.mosaic_bot.exceptions.CantConnectToServerException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
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
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer %s".formatted(getAccessToken(adminLogin, adminPassword).get()));
        headers.add("User-Agent", "TelegramBot");
        headers.add("Accept", "*/*");
        WebClient webClient = org.springframework.web.reactive.function.client.WebClient
                .builder()
                .defaultHeaders(httpHeaders -> httpHeaders.addAll(headers))
                .baseUrl(baseUrl)
                .build();
        String response = webClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path(CODES_PATH)
                        .build())
                .bodyValue(new CodesRequest(numberOfCodes, numberOfCodeReuse))
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

    public Optional<UserLoginInf> loginToService(String adminLogin, String adminPassword) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-Agent", "TelegramBot");
        headers.add("Accept", "*/*");
        WebClient webClient = org.springframework.web.reactive.function.client.WebClient
                .builder()
                .defaultHeaders(httpHeaders -> httpHeaders.addAll(headers))
                .baseUrl(baseUrl)
                .build();
        UserLoginInf userLoginInf;
        try {
            userLoginInf = webClient
                    .post()
                    .uri(uriBuilder -> uriBuilder
                            .path(LOGIN_PATH)
                            .build())
                    .bodyValue(new LoginRequest(adminLogin, adminPassword))
                    .retrieve()
                    .bodyToMono(UserLoginInf.class)
                    .block();
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().value() == 401) {
                return Optional.empty();
            } else {
                throw e;
            }
        }
        return Optional.of(userLoginInf);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class UserLoginInf {
        private String access_token;
        private String refresh_token;
        private UserInf user;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class UserInf {
        private UUID id;
        private String name;
        private String email;
        private String role;
        private String registeredAt;
    }
    private record LoginRequest(String login, String password) {

    }

    private record CodeInf(String id, int usage) {

    }

    private record CodesRequest(int count, int usage) {
    }
}
