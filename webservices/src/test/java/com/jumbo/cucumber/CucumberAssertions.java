package com.jumbo.cucumber;

import static org.assertj.core.api.Assertions.*;

import java.time.Duration;
import org.assertj.core.description.Description;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionFactory;
import org.awaitility.core.ThrowingRunnable;
import org.springframework.http.HttpStatus;

public final class CucumberAssertions {

  private static final CallDescription JSON_DESCRIPTION = new CallDescription();

  private CucumberAssertions() {
  }

  public static SyncResponseAsserter assertThatLastResponse() {
    return new SyncResponseAsserter();
  }

  public static AsyncResponseAsserter assertThatLastAsyncResponse() {
    return assertThatLastAsyncResponse(Duration.ofSeconds(5));
  }

  public static AsyncResponseAsserter assertThatLastAsyncResponse(Duration maxDelay) {
    assertThat(maxDelay).as("Can't check async responses without maxDelay").isNotNull();

    return new AsyncResponseAsserter(maxDelay);
  }

  public static class SyncResponseAsserter implements ResponseAsserter<SyncResponseAsserter> {

    private SyncResponseAsserter() {
    }

    @Override
    public SyncResponseAsserter hasHttpStatus(HttpStatus status) {
      assertHttpStatus(status);

      return this;
    }

    @Override
    public ElementAsserter<SyncResponseAsserter> hasElement(String jsonPath) {
      return new ElementAsserter<>(this, jsonPath);
    }

    public SyncResponseAsserter doNotHaveElement(String jsonPath) {
      int elementsCount = CucumberTestContext.countEntries(jsonPath);

      assertThat(elementsCount).as("Expecting " + jsonPath + " to not exists " + callContext()).isEqualTo(0);

      return this;
    }
  }

  public static class AsyncResponseAsserter implements ResponseAsserter<AsyncResponseAsserter> {

    private final Duration maxTime;

    private AsyncResponseAsserter(Duration maxTime) {
      this.maxTime = maxTime;
    }

    @Override
    public AsyncResponseAsserter hasHttpStatus(HttpStatus status) {
      await(maxTime, () -> assertHttpStatus(status));

      return this;
    }

    @Override
    public ElementAsserter<AsyncResponseAsserter> hasElement(String jsonPath) {
      return new AsyncElementAsserter(this, jsonPath, maxTime);
    }
  }

  public static class AsyncElementAsserter extends ElementAsserter<AsyncResponseAsserter> {

    private final Duration maxTime;

    private AsyncElementAsserter(AsyncResponseAsserter responseAsserter, String jsonPath, Duration maxTime) {
      super(responseAsserter, jsonPath);
      this.maxTime = maxTime;
    }

    @Override
    protected void assertPathValue(String jsonPath, Object value) {
      await(maxTime, () -> super.assertPathValue(jsonPath, value));
    }

    @Override
    public AsyncElementAsserter withElementsCount(int count) {
      await(maxTime, () -> super.withElementsCount(count));

      return this;
    }
  }

  private static void assertHttpStatus(HttpStatus status) {
    assertThat(CucumberTestContext.getStatus())
      .as("Expecting request to result in " + status + " but got " + CucumberTestContext.getStatus() + callContext())
      .isEqualTo(status);
  }

  private static void await(Duration maxTime, ThrowingRunnable assertion) {
    awaiter(maxTime)
      .untilAsserted(
        () -> {
          try {
            assertion.run();
          } catch (AssertionError e) {
            CucumberTestContext.retry();

            assertion.run();
          }
        }
      );
  }

  private static ConditionFactory awaiter(Duration maxTime) {
    return Awaitility.await().pollDelay(Duration.ofMillis(0)).pollInterval(Duration.ofMillis(100)).atMost(maxTime);
  }

  static Description callContext() {
    return JSON_DESCRIPTION;
  }

  private static class CallDescription extends Description {

    @Override
    public String value() {
      return (
        " from " +
          CucumberTestContext.getUri() +
          " in " +
          System.lineSeparator() +
          CucumberTestContext.getResponse().map(JsonHelper::pretty).orElse("empty")
      );
    }
  }
}
