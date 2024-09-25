package br.com.itau.insurance_quote.domain;

import br.com.itau.insurance_quote.domain.event.DomainEvent;
import br.com.itau.insurance_quote.domain.validation.ValidationHandler;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("unitTest")
class EntityTest {

    @Test
    void givenNullASEvents_whenInstantiate_shouldBeOk() {
        final List<DomainEvent> events = null;

        final var entity = new DummyEntity(new DummyID(1L), events);

        assertNotNull(entity.getDomainEvents());
        assertTrue(entity.getDomainEvents().isEmpty());
    }

    @Test
    void givenDomainEvents_whenPassInConstructor_shouldCreateADefensiveClone() {
        final List<DomainEvent> events = new ArrayList<>();
        events.add(new DummyEvent());

        final var entity = new DummyEntity(new DummyID(1L), events);

        assertNotNull(entity.getDomainEvents());
        assertEquals(1, entity.getDomainEvents().size());

        assertThrows(RuntimeException.class, () -> {
            final var actualEvents = entity.getDomainEvents();
            actualEvents.add(new DummyEvent());
        });
    }

    @Test
    void givenDomainEvents_whenPCallsRegisterEvent_shouldAddEventToList() {
        final var expectedEvents = 1;
        final var entity = new DummyEntity(new DummyID(1L), new ArrayList<>());

        entity.registerEvent(new DummyEvent());

        assertNotNull(entity.getDomainEvents());
        assertEquals(expectedEvents, entity.getDomainEvents().size());
    }

    @Test
    void givenAFewDomainEvents_whenPCallsPublishsEvent_shouldCallPublisherAndClearTheList() {
        final var expectedEvents = 0;
        final var expectedSentEvents = 2;
        final var counter = new AtomicInteger(0);
        final var entity = new DummyEntity(new DummyID(1L), new ArrayList<>());
        entity.registerEvent(new DummyEvent());
        entity.registerEvent(new DummyEvent());

        assertEquals(2, entity.getDomainEvents().size());

        entity.publishDomainEvents(event -> {
            counter.incrementAndGet();
        });

        assertNotNull(entity.getDomainEvents());
        assertEquals(expectedEvents, entity.getDomainEvents().size());
        assertEquals(expectedSentEvents, counter.get());
    }

    public static class DummyEvent implements DomainEvent {

        @Override
        public Instant occurredOn() {
            return Instant.now();
        }
    }

    public static class DummyID extends Identifier<Long> {
        protected DummyID(Long value) {
            super(value);
        }
    }

    public static class DummyEntity extends Entity<DummyID> {

        public DummyEntity() {
            this(new DummyID(1L), null);
        }

        public DummyEntity(DummyID dummyID, List<DomainEvent> domainEvents) {
            super(dummyID, domainEvents);
        }

        @Override
        public void validate(ValidationHandler handler) {

        }
    }
}
