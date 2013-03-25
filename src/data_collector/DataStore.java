package data_collector;

import java.io.IOException;
import java.io.InputStream;

/**
 * Interface to the persistence layer.
 */
public interface DataStore {
  /**
   * Store the data provided by this InputStream.  This call must be thread-safe.
   * @param data data to persist
   * @param callId the id of the call this data relates to
   * @param clientId the id of the client sending this data
   * @param attemptId the client attempt count
   * @throws IOException if unable to write the data.
   */
  void store(InputStream data, String callId, String clientId, int attemptId) throws IOException;
}
