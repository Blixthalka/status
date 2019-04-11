package tech.blixthalka.mapz.adapters.sonar;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import tech.blixthalka.mapz.CodeQualityMetrics;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@JsonRootName(value = "component")
class CodeQualityDTO {
    String id;
    String key;
    List<Measure> measures;

    @Data
    private static class Measure {
        String metric;
        String value;
        String bestValue;
    }

    CodeQualityMetrics toCodeQualityMetrics() {
        Map<String, String> metrics = measures.stream()
                .collect(Collectors.toMap(Measure::getMetric, Measure::getValue));
        return new CodeQualityMetrics(metrics);
    }
}
