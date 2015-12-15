package com.rafaelfiume.salume.web.result;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.rafaelfiume.salume.domain.Reputation.NORMAL;
import static com.rafaelfiume.salume.domain.Reputation.TRADITIONAL;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ReputationRepresentationTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldDisplayNormalReputationAsSpecialToClient() {
        assertThat(ReputationRepresentation.of(NORMAL),      is("special"));
        assertThat(ReputationRepresentation.of(TRADITIONAL), is("traditional"));
    }

    @Test
    public void shouldThrowExceptionWhenTryingToGetARepresentationOfAnUnknownReputation() {
        exception.expect(NullPointerException.class);
        ReputationRepresentation.of(null);
    }
}
