package com.fintracker.backend.fintrackermonolith.exception.support;

public class SupportRequestNotFoundException extends RuntimeException {
    public SupportRequestNotFoundException () {
        super("SupportRequest not found");

    }
}
