package org.whispersystems.data_collector;

import com.google.common.collect.ImmutableList;

/**
 * Provides a list of questions to ask the user about call quality
 */
public class CallQualityQuestions {

  public static CallQualityConfig getQuestions() {
    return new CallQualityConfig(ImmutableList.of("Echo", "Dropped Call", "Low Volume", "Delayed Audio"));
  }
}
