package com.rafaelfiume.salume.web.model;

import com.rafaelfiume.salume.domain.Reputation;

import static java.lang.String.format;

public final class ReputationResponseModel {

    private ReputationResponseModel() {
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
