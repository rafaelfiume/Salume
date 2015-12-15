package com.rafaelfiume.salume.web.result;

import com.rafaelfiume.salume.domain.Reputation;

import static java.lang.String.format;

public final class ReputationRepresentation {

    private ReputationRepresentation() {
        // Not intended to be instantiate
    }

    public static String of(Reputation reputation) {
        switch (reputation) {
            case NORMAL:      return "special";
            case TRADITIONAL: return "traditional";
        }

        throw new IllegalArgumentException(format("unknown reputation %s", reputation));
    }
}
