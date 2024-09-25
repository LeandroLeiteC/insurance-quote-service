package br.com.itau.insurance_quote.infrastructure.quote.persistence.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Set;

@Converter
public class AssistancesConverter implements AttributeConverter<Set<String>, String> {

    @Override
    public String convertToDatabaseColumn(Set<String> attribute) {
        return attribute.stream().reduce((a, b) -> a + ";" + b).get();
    }

    @Override
    public Set<String> convertToEntityAttribute(String dbData) {
        String[] split = dbData.split(";");
        return Set.of(split);
    }
}
