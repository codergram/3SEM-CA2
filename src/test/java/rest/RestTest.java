package rest;

import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.Person;
import entities.Phone;
import java.util.ArrayList;
import java.util.List;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.net.URI;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
class RestTest {

  private static final int SERVER_PORT = 7777;
  private static final String SERVER_URL = "http://localhost/api";

  static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
  private static HttpServer httpServer;
  private static EntityManagerFactory emf;

  //Entities for the tests
  Address a1, a2;
  CityInfo c1, c2;
  Hobby h1, h2;
  Person p1, p2;
  Phone ph1, ph2;

  static HttpServer startServer() {
    ResourceConfig rc = ResourceConfig.forApplication(new Api());
    return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
  }

  @BeforeAll
  public static void setUpClass() {
    //This method must be called before you request the EntityManagerFactory
    EMF_Creator.startREST_TestWithDB();
    emf = EMF_Creator.createEntityManagerFactoryForTest();

    httpServer = startServer();
    //Setup RestAssured
    RestAssured.baseURI = SERVER_URL;
    RestAssured.port = SERVER_PORT;
    RestAssured.defaultParser = Parser.JSON;
  }

  @AfterAll
  public static void closeTestServer() {
    //System.in.read();

    //Don't forget this, if you called its counterpart in @BeforeAll
    EMF_Creator.endREST_TestWithDB();
    httpServer.shutdownNow();
  }

  @BeforeEach
  public void setUp() {
    EntityManager em = emf.createEntityManager();

    c1 = new CityInfo("4600", "Køge");
    c2 = new CityInfo("4000", "Roskilde");

    a1 = new Address("Glentevej 4", c1);
    a2 = new Address("Algade 22", c2);

    h1 = new Hobby("Skak");
    h2 = new Hobby("Golf");

    List<Hobby> hl1 = new ArrayList<>();
    hl1.add(h1);
    List<Hobby> hl2 = new ArrayList<>();
    hl2.add(h1);
    hl2.add(h2);

    List<Phone> phl1 = new ArrayList<>();
    List<Phone> phl2 = new ArrayList<>();
    ph1 = new Phone(20222022);
    ph2 = new Phone(88888888);
    phl1.add(ph1);
    phl2.add(ph2);


    p1 = new Person("a@b.dk", "John", "Andersen", phl1, a1, hl1);
    p2 = new Person("c@d.dk", "Kurt", "Verner", phl2, a2, hl2);

    try {
      //Person 1
      em.getTransaction().begin();
      //Address
      em.persist(c1);
      a1.setCityInfo(c1);
      em.persist(a1);
      //Phone
      em.persist(ph1);
      ph1.setPerson(p1);
      em.merge(ph1);
      //
      em.persist(p1);
      //Hobbies
      em.persist(h1);
      p1.addHobby(h1);
      em.merge(p1);
      //
      em.getTransaction().commit();


      //Person 2
      em.getTransaction().begin();
      //Address
      em.persist(c2);
      a2.setCityInfo(c2);
      em.persist(a2);
      //Phone
      em.persist(ph2);
      ph2.setPerson(p2);
      em.merge(ph2);
      //
      em.persist(p2);
      //Hobbies
      em.persist(h2);
      p2.addHobby(h1);
      p2.addHobby(h2);
      em.merge(p2);
      //
      em.getTransaction().commit();

    } finally {
      em.close();
    }
    }

    @Test
    void testCount() throws Exception {
      given()
          .contentType("application/json")
          .get("/count/persons")
          .then()
          .assertThat()
          .statusCode(HttpStatus.OK_200.getStatusCode())
          .body("size()", equalTo(2));
    }

    @Test
    void testGetByName() throws Exception {
      String expectedName = p1.getFirstName();

      given()
          .pathParam("zip", "4600")
          .contentType("application/json")
          .get("/person/zip/{zip}")
          .then()
          .assertThat()
          .statusCode(HttpStatus.OK_200.getStatusCode())
          .body("firstName", equalToIgnoringCase(expectedName));
    }
  }
