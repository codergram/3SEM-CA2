package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.HobbyDTO;
import dtos.PersonDTO;
import entities.CityInfo;
import entities.Hobby;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import utils.EMF_Creator;
import facades.PersonFacade;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("person")
public class PersonResource {

  private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

  private static final PersonFacade FACADE = PersonFacade.getFacade(EMF);
  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public String demo() {
    return "{\"msg\":\"Hello World\"}";
  }

  @Path("{id}")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getPersonById(@PathParam("id") long id) {
    PersonDTO dto = FACADE.getPersonById(id);
    System.out.println("DTO found: " + dto);
    //String gson = GSON.toJson(dto, PersonDTO.class);
    //System.out.println("GSON: " + gson);
    return Response.ok(dto, MediaType.APPLICATION_JSON).build();
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

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public PersonDTO createNewPerson(PersonDTO p) {
    System.out.println("Got: " + p);
    return FACADE.createPerson(p);

  }

  @Path("{id}/hobby")
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public PersonDTO addHobbyToPerson(@PathParam("id") long id, HobbyDTO hobby) {
    return FACADE.addHobbyToPerson(hobby, id);

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
