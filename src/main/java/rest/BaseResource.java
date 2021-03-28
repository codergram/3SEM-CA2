package rest;

import com.google.gson.Gson;
import dtos.CityInfoDTO;
import dtos.PersonDTO;
import entities.Hobby;
import facades.MainFacade;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import utils.EMF_Creator;
import utils.Utility;

@OpenAPIDefinition(
    info = @Info(
        title = "CA2 API",
        version = "1.0",
        description = "Simple API to use as start code for backend web projects.",
        contact = @Contact( name = "Emil Elkjær Nielsne", email = "cph-en93@cphbusiness.dk")
    ),
    tags = {
        @Tag(name = "base", description = "API base"),
        @Tag(name = "person", description = "Persons API")

    },
    servers = {
        @Server(
            description = "For Local host testing",
            url = "http://localhost:8080/ca2"
        ),
        @Server(
            description = "Server API",
            url = "https://api.codergram.me/ca2"
        )

    }
)
@Path("")
public class BaseResource {

  private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
  private static final Gson GSON = Utility.GSON;
  private static final MainFacade FACADE = MainFacade.getFacade(EMF);

  @Operation(summary = "Test connection",
      tags = {"base"})
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public String alive() {
    return "{\"msg\":\"I am alive\"}";
  }

  @Operation(summary = "Get a list of all persons",
      tags = {"person"},
      responses = {
          @ApiResponse(
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = PersonDTO.class))
          ),
          @ApiResponse(responseCode = "200", description = "List of persons"),
          @ApiResponse(responseCode = "400", description = "No persons found")})
  @Path("persons")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public String getAllPersons(){
    return GSON.toJson(FACADE.getAllPersons());
  }

  @Operation(summary = "Get a list of all zip codes",
      tags = {"base"},
      responses = {
          @ApiResponse(
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = CityInfoDTO.class))
          ),
          @ApiResponse(responseCode = "200", description = "List of zips"),
          @ApiResponse(responseCode = "400", description = "No zips found")})
  @Path("zip/all")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public String getListOfZips() {
    return GSON.toJson(FACADE.getAllZips());
  }

  @Operation(summary = "Count persons with given hobby",
      tags = {"person"},
      responses = {
          @ApiResponse(
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = PersonDTO.class))
          ),
          @ApiResponse(responseCode = "200", description = "Number of persons with hobby"),
          @ApiResponse(responseCode = "400", description = "No persons found")})
  @Path("count/hobby/persons")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public String getPersonCountByHobby(@QueryParam("hobby") String hobby) {
    return GSON.toJson(FACADE.getPersonCountByHobby(new Hobby(hobby)));
  }

  @Operation(summary = "Count persons in database",
      tags = {"person"},
      responses = {
          @ApiResponse(
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = PersonDTO.class))
          ),
          @ApiResponse(responseCode = "200", description = "Number of persons"),
          @ApiResponse(responseCode = "400", description = "No persons found")})
  @Path("count/persons")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public String getPersonCount() {
    return GSON.toJson(FACADE.getPersonCount());
  }

}
