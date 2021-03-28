package rest;

import com.google.gson.Gson;
import dtos.PersonDTO;
import entities.CityInfo;
import facades.MainFacade;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
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


  @Operation(summary = "Get person by ID",
      tags = {"person"},
      responses = {
          @ApiResponse(
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = PersonDTO.class))
          ),
          @ApiResponse(responseCode = "200", description = "Found person"),
          @ApiResponse(responseCode = "400", description = "No persons found")})
  @Path("{id}")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public String getPersonById(
      @ApiParam("The persons ID")
      @PathParam("id") long id) {
    return GSON.toJson(FACADE.getPersonById(id), PersonDTO.class);
  }

  @Operation(summary = "Get person by their hobby",
      tags = {"person"},
      parameters = @Parameter(name = "hobby name", required = true),
      responses = {
          @ApiResponse(
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = PersonDTO.class))
          ),
          @ApiResponse(responseCode = "200", description = "Found person"),
          @ApiResponse(responseCode = "400", description = "No persons found")})
  //Using QueryParam instead of PathParam since hobby is not a resource, but a property of a resource
  @Path("hobby")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public String getPersonByHobby(
      @ApiParam("The name of the hobby we are searching for")
      @QueryParam("hobby") String hobby) {
    return GSON.toJson(FACADE.getPersonListByHobby(hobby));
  }

  @Operation(summary = "Get person by their zip code",
      tags = {"person"},
      parameters = @Parameter(name = "id", required = true),
      responses = {
          @ApiResponse(
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = PersonDTO.class))
          ),
          @ApiResponse(responseCode = "200", description = "Found person"),
          @ApiResponse(responseCode = "400", description = "No persons found")})
  @Path("zip")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public String getPersonByZip(
      @ApiParam("The zip we're sorting on")
      @QueryParam("zip") String zip) {
    return GSON.toJson(FACADE.getPersonListByZip(new CityInfo(zip)));
  }


  @Operation(summary = "Create new person",
      tags = {"person"},
      requestBody = @RequestBody(
          description = "Person to add",
          required = true,
          content = @Content(
              schema = @Schema(implementation = PersonDTO.class)
          )
      ),
      responses = {
          @ApiResponse(
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = PersonDTO.class))
          ),
          @ApiResponse(responseCode = "200", description = "Person was created"),
          @ApiResponse(responseCode = "400", description = "Person was not created"),
          @ApiResponse(responseCode = "500", description = "Internal error")})
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public PersonDTO createNewPerson(PersonDTO p) {
    return FACADE.createPerson(p);

  }

  @Operation(summary = "Update a person",
      tags = {"person"},
      requestBody = @RequestBody(
          description = "Updated person object",
          required = true,
          content = @Content(
              schema = @Schema(implementation = PersonDTO.class)
          )
      ),
      responses = {
          @ApiResponse(
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = PersonDTO.class))
          ),
          @ApiResponse(responseCode = "200", description = "Person was created"),
          @ApiResponse(responseCode = "400", description = "Person was not created"),
          @ApiResponse(responseCode = "500", description = "Internal error")})
  @Path("{id}")
  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public PersonDTO updatePerson(@ApiParam("id of person to be updated") @PathParam("id") long id, PersonDTO p) {
    return FACADE.updatePerson(id, p);
  }

  @Operation(summary = "Delete a person",
      tags = {"person"},
      responses = {
          @ApiResponse(responseCode = "200", description = "Person was created"),
          @ApiResponse(responseCode = "400", description = "Person was not created"),
          @ApiResponse(responseCode = "500", description = "Internal error")})
  @Path("{id}")
  @DELETE
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public String editPerson(@PathParam("id") long id) {
    return "{\"result\":\"" + FACADE.deletePersonById(id) + "\"}";

  }

}
