package tech.blixthalka.mapz.adapters.sonar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tech.blixthalka.mapz.CodeQualityFetcher;
import tech.blixthalka.mapz.CodeQualityMetrics;

public class SonarCodeQualityFetcher implements CodeQualityFetcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(SonarCodeQualityFetcher.class);
    private final WebClient webClient;

    public SonarCodeQualityFetcher(final WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<CodeQualityMetrics> fetchCodeQuality(String project) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.scheme("https")
                        .host("sonarcloud.io")
                        .path("api/measures/component")
                        .queryParam("component", "com.trustly:" + project)
                        .queryParam("metricKeys", "coverage ,code_smells")
                        .build())
                .retrieve()
                .bodyToMono(CodeQualityDTO.class)
                .map(CodeQualityDTO::toCodeQualityMetrics)
                .doOnNext(metrics -> LOGGER.info("Fetched code quality={}", metrics))
                .doOnError(throwable -> LOGGER.info("Got error when fetching code quality", throwable));
    }
}
