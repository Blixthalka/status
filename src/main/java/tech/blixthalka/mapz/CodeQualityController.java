package tech.blixthalka.mapz;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@RestController("/api/code-quality")
public class CodeQualityController {
    private CodeQualityFetcher codeQualityFetcher;

    public CodeQualityController(CodeQualityFetcher codeQualityFetcher) {
        this.codeQualityFetcher = codeQualityFetcher;
    }

    @SuppressWarnings("unused")
    @RequestMapping(value = "/{project}")
    Mono<CodeQualityMetrics> index(@PathVariable String project) {
        return codeQualityFetcher.fetchCodeQuality(project);
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(WebClientResponseException exception) {
        if (exception.getStatusCode().is4xxClientError()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
