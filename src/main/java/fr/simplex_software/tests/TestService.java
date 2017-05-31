package fr.simplex_software.tests;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/api")
public interface TestService
{
  @GET
  @Path("r1")
  public Response testGetResource();
}
