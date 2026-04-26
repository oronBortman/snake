package com.snake;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Setup Verification")
class SetupVerificationTest {

    @Test
    @DisplayName("Maven test runner is configured and working")
    void mavenTestRunnerIsConfiguredAndWorking() {
        assertThat(true, equalTo(true));
    }
}
