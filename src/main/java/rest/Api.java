package rest;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Set;
import javax.ws.rs.core.Application;
import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;

@javax.ws.rs.ApplicationPath("api")
public class Api extends Application {

  @Override
  public Set<Class<?>> getClasses() {
    Set<Class<?>> resources = new java.util.HashSet<>();
    addRestResourceClasses(resources);

    //OpenAPI / Swagger.io
    resources.add(OpenApiResource.class);
    resources.add(AcceptHeaderOpenApiResource.class);

    return resources;
  }

  /**
   * Do not modify addRestResourceClasses() method.
   * It is automatically populated with
   * all resources defined in the project.
   * If required, comment out calling this method in getClasses().
   */
  private void addRestResourceClasses(Set<Class<?>> resources) {
    resources.add(cors.CorsFilter.class);
    resources.add(exceptions.ExceptionHandler.class);
    resources.add(org.glassfish.jersey.server.wadl.internal.WadlResource.class);
    resources.add(rest.BaseResource.class);
    resources.add(rest.PersonResource.class);
  }

}
