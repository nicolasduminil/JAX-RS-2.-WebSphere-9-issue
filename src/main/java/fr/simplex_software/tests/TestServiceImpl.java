package fr.simplex_software.tests;

import java.util.*;

import javax.ws.rs.core.*;

public class TestServiceImpl extends Application implements TestService
{
  public Response testGetResource()
  {
    return Response.ok().build();
  }

  @Override
  public Set<Class<?>> getClasses() 
  {
    Set<Class<?>> classes = new HashSet<Class<?>>();
    classes.add(TestServiceImpl.class);
    return classes;
  }
}
