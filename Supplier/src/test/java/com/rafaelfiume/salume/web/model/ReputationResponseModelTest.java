package com.rafaelfiume.salume.web.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.rafaelfiume.salume.domain.Reputation.NORMAL;
import static com.rafaelfiume.salume.domain.Reputation.TRADITIONAL;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ReputationResponseModelTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldDisplayNormalReputationAsSpecialToClient() {
        assertThat(ReputationResponseModel.of(NORMAL),      is("special"));
        assertThat(ReputationResponseModel.of(TRADITIONAL), is("traditional"));
    }

    @Test
    public void shouldThrowExceptionWhenTryingToGetARepresentationOfAnUnknownReputation() {
        exception.expect(NullPointerException.class);
        ReputationResponseModel.of(null);
    }
}
