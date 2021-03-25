package rest;

import com.google.gson.Gson;
import dtos.PersonDTO;
import entities.CityInfo;
import facades.MainFacade;
import javax.print.attribute.standard.Media;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import utils.EMF_Creator;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import utils.Utility;

@Path("person")
public class PersonResource {

  private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
  private static final Gson GSON = Utility.GSON;
  private static final MainFacade FACADE = MainFacade.getFacade(EMF);

  @Path("all")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public String getAllPersons(){
    return GSON.toJson(FACADE.getAllPersons());
  }

  @Path("{id}")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public String getPersonById(@PathParam("id") long id) {
    return GSON.toJson(FACADE.getPersonById(id), PersonDTO.class);
  }

  //Using QueryParam instead of PathParam since hobby is not a resource, but a property of a resource
  @Path("hobby")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public String getPersonByHobby(@QueryParam("hobby") String hobby) {
    return GSON.toJson(FACADE.getPersonListByHobby(hobby));
  }

  @Path("zip")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public String getPersonByZip(@QueryParam("zip") String zip) {
    return GSON.toJson(FACADE.getPersonListByZip(new CityInfo(zip)));
  }


  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public PersonDTO createNewPerson(PersonDTO p) {
    return FACADE.createPerson(p);

  }

  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public PersonDTO updatePerson(PersonDTO p) {
    return FACADE.updatePerson(p);
  }

  @Path("{id}")
  @DELETE
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public String editPerson(@PathParam("id") long id) {
    return "{\"result\":\"" + FACADE.deletePersonById(id) + "\"}";

  }

}
