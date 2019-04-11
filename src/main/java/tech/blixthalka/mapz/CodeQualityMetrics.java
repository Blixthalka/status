package tech.blixthalka.mapz;

import lombok.*;

import java.util.Map;

@Value
public class CodeQualityMetrics {
    Map<String, String> metrics;
}
