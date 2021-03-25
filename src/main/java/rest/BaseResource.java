package rest;

import com.google.gson.Gson;
import entities.Hobby;
import facades.MainFacade;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import utils.EMF_Creator;
import utils.Utility;

@Path("")
public class BaseResource {

  private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
  private static final Gson GSON = Utility.GSON;
  private static final MainFacade FACADE = MainFacade.getFacade(EMF);

  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public String alive() {
    return "{\"msg\":\"I am alive\"}";
  }

  @Path("zip/all")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public String getListOfZips() {
    return GSON.toJson(FACADE.getAllZips());
  }

  @Path("count/hobby/persons")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public String getPersonCountByHobby(@QueryParam("hobby") String hobby) {
    return GSON.toJson(FACADE.getPersonCountByHobby(new Hobby(hobby)));
  }

  @Path("count/persons")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public String getPersonCount() {
    return GSON.toJson(FACADE.getPersonCount());
  }

}
