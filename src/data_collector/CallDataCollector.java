package data_collector;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.multipart.BodyPart;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.MultiPart;
import com.sun.net.httpserver.HttpServer;
import com.sun.xml.internal.ws.api.message.Attachment;

/**
 * Allows data from RedPhone clients and the RedPhone server to be uploaded and stored.
 */
// The Java class will be hosted at the URI path "/helloworld"
@Path("/collector")
public class CallDataCollector {
  @DataStoreProvider.DataStoreResource
  private final DataStore store;

  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response postData(MultiPart attachments) {
    Map<String, FormDataBodyPart> parts = Maps.newHashMap();
    try {
      for(BodyPart part : attachments.getBodyParts()) {
        FormDataBodyPart formPart = (FormDataBodyPart)part;
        parts.put(formPart.getName(), formPart);
      }

      String clientId = parts.get("client_id").getValue();
      String callId = parts.get("call_id").getValue();
      int attempt = Integer.parseInt(parts.get("attempt_id").getValue());
      InputStream data = parts.get("payload").getValueAs(InputStream.class);

      validateId(callId);
      validateId(clientId);
      store.store(data, callId, clientId, attempt);

    } catch (Exception e) {
      e.printStackTrace();
      throw new WebApplicationException(
          Response.status(Response.Status.BAD_REQUEST).build());
    }
    return Response.ok().build();
  }

  public CallDataCollector() {
    this.store = null;
  }

  private void validateId(String rawId) {
    Preconditions.checkNotNull(rawId);
    if (rawId.matches("^[^a-zA-Z0-9]+$")) {
      throw new IllegalArgumentException("ID: " + rawId + " contains an illegal character");
    }
  }

  public static void main(String[] args) throws IOException {
        HttpServer server = HttpServerFactory.create("http://localhost:9998/");
        server.start();

        System.out.println("Server running");
        System.out.println("Visit: http://localhost:9998/helloworld");
        System.out.println("Hit return to stop...");
        System.in.read();
        System.out.println("Stopping server");
        server.stop(0);
        System.out.println("Server stopped");
    }
}