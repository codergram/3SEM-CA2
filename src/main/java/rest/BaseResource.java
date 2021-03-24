package rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("")
public class BaseResource {

  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public String alive() {
    return "{\"msg\":\"I am alive\"}";
  }

}
