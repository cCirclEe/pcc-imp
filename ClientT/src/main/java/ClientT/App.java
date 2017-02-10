package ClientT;


import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Hello world!
 *
 */
public class App 
{
    private static String accountData = "{\n" +
        "  \"account\": {\n" +
        "    \"mail\": \"test@example.de\",\n" +
        "    \"password\": \"passwordTest\"\n" +
        "  }\n" +
        "}";
    private static String newAccountData = "{\n" +
            "  \"account\": {\n" +
            "    \"mail\": \"test2@example.de\",\n" +
            "    \"password\": \"passwordTest\"\n" +
            "  }\n" +
            "}";
    private static String uuid = "1200";
    private static final String MAINADRESS = "http://laubenstone.de:2222/";

    public static void main( String[] args ) {
        createAccount();
        verifyAccount();
        authenticate();
        changeAccount();
        deleteAccount();
    }

    private static void changeAccount() {
        Form f = new Form();
        f.param("data", accountData);
        f.param("newData", newAccountData);
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(MAINADRESS).path("webservice").path("changeAccount");
        System.out.println(webTarget.getUri());
        Response response = webTarget.request().post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE),Response.class);
        System.out.println(response.readEntity(String.class));
        accountData = newAccountData;
    }

    private static void createAccount() {
        Form f = new Form();
        f.param("data", accountData);
        f.param("uuid", uuid);
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(MAINADRESS).path("webservice").path("createAccount");
        System.out.println(webTarget.getUri());
        Response response = webTarget.request().post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE),Response.class);
        System.out.println(response.readEntity(String.class));
    }

    private static void deleteAccount () {
        Form f = new Form();
        f.param("data", accountData);
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(MAINADRESS).path("webservice").path("deleteAccount");
        System.out.println(webTarget.getUri());
        Response response = webTarget.request().post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE),Response.class);
        System.out.println(response.readEntity(String.class));
    }

    private static void verifyAccount () {
        Form f = new Form();
        f.param("data", accountData);
        f.param("uuid", uuid);
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(MAINADRESS).path("webservice").path("verifyAccount");
        System.out.println(webTarget.getUri());
        Response response = webTarget.request().post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE),Response.class);
        System.out.println(response.readEntity(String.class));
    }

    private static void authenticate() {
        Form f = new Form();
        f.param("data", accountData);
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(MAINADRESS).path("webservice").path("authenticate");
        System.out.println(webTarget.getUri());
        Response response = webTarget.request().post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE),Response.class);
        System.out.println(response.readEntity(String.class));
    }
}
