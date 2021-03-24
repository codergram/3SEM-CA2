package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.PersonDTO;
import entities.CityInfo;
import entities.Hobby;
import facades.MainFacade;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import utils.EMF_Creator;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("person")
public class PersonResource {

  private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

  private static final MainFacade FACADE = MainFacade.getFacade(EMF);
  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

  @Path("{id}")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public String getPersonById(@PathParam("id") long id) {
    return GSON.toJson(FACADE.getPersonById(id), PersonDTO.class);
  }

  @Path("hobby/{hobby}")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public String getPersonByHobby(@PathParam("hobby") String hobby) {
    return GSON.toJson(FACADE.getPersonListByHobby(new Hobby(hobby)));
  }

  @Path("address/zip/{zip}")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public String getPersonByZip(@PathParam("zip") String zip) {
    return GSON.toJson(FACADE.getPersonListByZip(new CityInfo(zip)));
  }

  @Path("count/hobby/{hobby}")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public String getPersonCountByHobby(@PathParam("hobby") String hobby) {
    return GSON.toJson(FACADE.getPersonCountByHobby(new Hobby(hobby)));
  }

  @Path("address/zip")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public String getListOfZips() {
    return GSON.toJson(FACADE.getAllZips());
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public PersonDTO createNewPerson(PersonDTO p) {
    return FACADE.createPerson(p);

  }

  @Path("{id}")
  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public PersonDTO editPerson(@PathParam("id") long id, PersonDTO p) {
    return FACADE.editPerson(p, id);

  }

  @Path("{id}")
  @DELETE
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public String editPerson(@PathParam("id") long id) {
    return "{\"result\":\"" + FACADE.deletePersonById(id) + "\"}";

  }

}
