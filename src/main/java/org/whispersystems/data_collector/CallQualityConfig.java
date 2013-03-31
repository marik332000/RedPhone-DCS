package org.whispersystems.data_collector;

import java.util.List;

public class CallQualityConfig {
  private final List<String> callQualityQuestions;

  public CallQualityConfig(List<String> callQualityQuestions) {
    this.callQualityQuestions = callQualityQuestions;
  }

  public List<String> getCallQualityQuestions() {
    return callQualityQuestions;
  }
}