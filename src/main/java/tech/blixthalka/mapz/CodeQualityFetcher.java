package tech.blixthalka.mapz;

import reactor.core.publisher.Mono;

public interface CodeQualityFetcher {

    Mono<CodeQualityMetrics> fetchCodeQuality();

}
