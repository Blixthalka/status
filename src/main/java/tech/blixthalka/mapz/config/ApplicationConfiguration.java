package tech.blixthalka.mapz.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import tech.blixthalka.mapz.CodeQualityFetcher;
import tech.blixthalka.mapz.adapters.sonar.SonarCodeQualityFetcher;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Configuration
public class ApplicationConfiguration {

    private SonarConfigurationProperties sonarConfigurationProperties;

    public ApplicationConfiguration(SonarConfigurationProperties sonarConfigurationProperties) {
        this.sonarConfigurationProperties = sonarConfigurationProperties;
    }

    @Bean
    CodeQualityFetcher sonarCodeQualityFetcher(WebClient webClient) {
        return new SonarCodeQualityFetcher(webClient, sonarConfigurationProperties.getToken());
    }

    @Bean
    WebClient webClient() {
        ObjectMapper objectMapper = new ObjectMapper()
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        final ExchangeStrategies strategies = ExchangeStrategies.builder().codecs(configurer -> {
            configurer.registerDefaults(false);
            configurer.customCodecs().encoder(new Jackson2JsonEncoder(objectMapper, APPLICATION_JSON));
            configurer.customCodecs().decoder(new Jackson2JsonDecoder(objectMapper, APPLICATION_JSON));
        }).build();

        return  WebClient.builder()
                .exchangeStrategies(strategies)
                .build();
    }
}
