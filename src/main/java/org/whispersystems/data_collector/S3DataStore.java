package org.whispersystems.data_collector;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;

/**
 * Data storage that uses S3
 */
public class S3DataStore implements DataStore {
  private final AmazonS3Client s3Client = new AmazonS3Client();
  private final String BUCKET_NAME = "org.whispersystems.redphone.datacollection";

  private final ThreadLocal<DateFormat> dateFormat = new ThreadLocal<DateFormat>() {
    @Override
    protected DateFormat initialValue() {
      return new SimpleDateFormat("YYYY/MM/dd");
    }
  };

  @Override
  public void store(InputStream data, String callId, String clientId, String dataSource, int attemptId) throws IOException {
    String key = String.format("%s/%s/%s/calldata-%s-%d", dateFormat.get().format(new Date()),
        callId, clientId, dataSource, attemptId);
    ObjectMetadata metadata = new ObjectMetadata();
    s3Client.putObject(BUCKET_NAME, key, data, metadata);
  }
}