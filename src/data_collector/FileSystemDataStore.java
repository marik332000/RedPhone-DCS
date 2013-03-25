package data_collector;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;

/**
 * DataStore that uses the filesystem.  File are stored in
 *
 * root/YYYY/MM/DD/CallId/ClientId/attemptId.dat
 */
public class FileSystemDataStore implements DataStore {
  private final File rootDir;
  private final ThreadLocal<DateFormat> dateFormat = new ThreadLocal<DateFormat>() {
    @Override
    protected DateFormat initialValue() {
      return new SimpleDateFormat("YYYY/MM/dd");
    }
  };

  public FileSystemDataStore(File rootDir) {
    Preconditions.checkArgument(rootDir.isDirectory());
    this.rootDir = rootDir;
  }

  @Override
  public void store(InputStream data, String callId, String clientId, int attemptId)
      throws IOException{
    String datePath = dateFormat.get().format(new Date());
    File storageDir = new File(rootDir, Joiner.on("/").join(datePath, callId, clientId));

    if (!storageDir.exists()) {
      storageDir.mkdirs();
    }

    Path dataFile = new File(storageDir, "data-" + attemptId).toPath();
    Files.copy(data, dataFile);
  }
}
