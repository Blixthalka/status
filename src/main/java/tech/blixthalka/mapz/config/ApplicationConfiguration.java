package tech.blixthalka.mapz.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import tech.blixthalka.mapz.CodeQualityFetcher;
import tech.blixthalka.mapz.adapters.sonar.SonarCodeQualityFetcher;

import java.nio.charset.StandardCharsets;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Configuration
@EnableConfigurationProperties({SonarConfigurationProperties.class})
public class ApplicationConfiguration {

    private SonarConfigurationProperties sonarConfigurationProperties;

    public ApplicationConfiguration(SonarConfigurationProperties sonarConfigurationProperties) {
        this.sonarConfigurationProperties = sonarConfigurationProperties;
    }

    @Bean
    CodeQualityFetcher sonarCodeQualityFetcher(WebClient sonarWebClient) {
        return new SonarCodeQualityFetcher(sonarWebClient);
    }

    @Bean
    WebClient webClient() {
        ObjectMapper objectMapper = new ObjectMapper()
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);

        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> {
                    configurer.registerDefaults(false);
                    configurer.customCodecs().encoder(new Jackson2JsonEncoder(objectMapper, APPLICATION_JSON));
                    configurer.customCodecs().decoder(new Jackson2JsonDecoder(objectMapper, APPLICATION_JSON));
                }).build();

        byte[] token = (sonarConfigurationProperties.getToken() + ":").getBytes(StandardCharsets.UTF_8);
        String tokenBase64 = Base64Utils.encodeToString(token);
        return WebClient.builder()
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " + tokenBase64)
                .exchangeStrategies(strategies)
                .build();
    }
}
