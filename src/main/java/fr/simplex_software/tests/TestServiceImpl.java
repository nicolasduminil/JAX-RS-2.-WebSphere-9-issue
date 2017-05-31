package fr.simplex_software.tests;

import java.util.*;

import javax.ws.rs.core.*;

public class TestServiceImpl implements TestService
{
  public Response testGetResource()
  {
    return Response.ok().build();
  }
}
