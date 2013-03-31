package org.whispersystems.data_collector;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;

/**
 * Allows data from RedPhone clients and the RedPhone server to be uploaded and stored.
 */
// The Java class will be hosted at the URI path "/helloworld"
@Path("/collector")
public class CallDataCollector {
  @DataStoreProvider.DataStoreResource
  private final DataStore store;
  private final Gson gson = new Gson();


  @POST
  @Path("/{call_id}/{client_id}/{data_source}/{attempt_id}")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response postData(@PathParam("call_id") String callId,
                           @PathParam("client_id") String clientId,
                           @PathParam("attempt_id") String attemptId,
                           @PathParam("data_source") String dataSource,
                           InputStream json) {
    try {
      int attempt = Integer.parseInt(attemptId);

      validateId(callId);
      validateId(clientId);
      validateId(dataSource);
      store.store(json, callId, clientId, dataSource, attempt);

    } catch (Exception e) {
      e.printStackTrace();
      throw new WebApplicationException(
          Response.status(Response.Status.BAD_REQUEST).build());
    }
    return Response.ok().build();
  }

  @GET
  @Path("/call_quality_questions")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getCallQualityQuestions() {
    return Response.ok(gson.toJson(CallQualityQuestions.getQuestions())).build();
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