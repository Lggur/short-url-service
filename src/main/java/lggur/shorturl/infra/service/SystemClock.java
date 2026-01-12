package lggur.shorturl.infra.service;

import lggur.shorturl.application.ports.Clock;

import java.time.Instant;

public class SystemClock implements Clock {

    @Override
    public Instant now() {
        return Instant.now();
    }
}
