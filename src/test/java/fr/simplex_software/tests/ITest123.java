package fr.simplex_software.tests;

import static org.junit.Assert.*;

import java.net.*;

import javax.ws.rs.client.*;
import javax.ws.rs.core.*;

import org.junit.*;

public class ITest123
{
  private static Client client;

  @BeforeClass
  public static void beforeClass()
  {
    client = ClientBuilder.newClient();
  }

  @AfterClass
  public static void afterClass()
  {
    client.close();
    client = null;
  }

  @Test
  public void testMultipart2() throws Exception
  {
    Response resp = client.target(new URI("http://localhost:9081/test/services/api/r1")).request().get();
    assertEquals (200, resp.getStatus());
  }
}
