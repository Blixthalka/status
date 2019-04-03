package tech.blixthalka.mapz.adapters.sonar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tech.blixthalka.mapz.CodeQualityFetcher;
import tech.blixthalka.mapz.CodeQualityMetrics;

public class SonarCodeQualityFetcher implements CodeQualityFetcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(SonarCodeQualityFetcher.class);
    private WebClient webClient;
    private String token;

    public SonarCodeQualityFetcher(WebClient webClient, String token) {
        this.webClient = webClient;
        this.token = token;
    }

    @Override
    public Mono<CodeQualityMetrics> fetchCodeQuality() {
        return webClient.get()
                .uri("https://sonarcloud.io/api/measures/component?component=com.trustly@wapi-integration&metricKeys=violations")
                .header("Authorization: Basic " + token)
                .exchange()
                .map(SonarCodeQualityFetcher::toCodeQualityMetrics);
    }

    private static CodeQualityMetrics toCodeQualityMetrics(ClientResponse clientResponse) {
        LOGGER.info("Got response with status code={}", clientResponse.statusCode());
        return new CodeQualityMetrics();
    }
}
