package tech.blixthalka.mapz;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FrontendController {
    private CodeQualityFetcher codeQualityFetcher;

    public FrontendController(CodeQualityFetcher codeQualityFetcher) {
        this.codeQualityFetcher = codeQualityFetcher;
    }


    @RequestMapping(value = "/")
    Mono<CodeQualityMetrics> index() {
        return codeQualityFetcher.fetchCodeQuality();
    }
}
