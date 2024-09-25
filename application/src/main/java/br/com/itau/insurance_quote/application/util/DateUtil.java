package br.com.itau.insurance_quote.application.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public final class DateUtil {
    private DateUtil() {
    }

    public static LocalDateTime toLocalDateTime(final Instant instant) {
        final var zoneId = ZoneId.of("America/Sao_Paulo");
        return instant.atZone(zoneId).toLocalDateTime();
    }
}
