package tech.blixthalka.mapz;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.thymeleaf.spring5.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;

import java.util.stream.Stream;

@Controller
public class FrontendController {
    private CodeQualityFetcher codeQualityFetcher;

    public FrontendController(CodeQualityFetcher codeQualityFetcher) {
        this.codeQualityFetcher = codeQualityFetcher;
    }

    @GetMapping("/")
    String index(Model model) {
        Stream.of("wapi-manager")
                .forEach(s -> {
                    IReactiveDataDriverContextVariable variable =
                            new ReactiveDataDriverContextVariable(codeQualityFetcher.fetchCodeQuality(s).flux(), 1);
                    model.addAttribute(s, variable);
                });


        return "index";
    }

}
