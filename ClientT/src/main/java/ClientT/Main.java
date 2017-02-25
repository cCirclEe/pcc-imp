package ClientT;


import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.json.JSONObject;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Hello world!
 *
 */
public class Main
{
    //local and server address
    private static final String HOST = "http://localhost:2222/webservice/";
    private static final String LOCAL_HOST = "http://laubenstone.de:2222/webservice/";
    private static String accountData;
    private static String newAccountData;
    //change uuid for different accounts
    private static String uuid = "456asdasd-asf43a3-asd3";

    public static void main( String[] args ) {
        // every request needs a setup
        setUp();

        //different requests here
        upload();

    }

    private static void setUp() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mail", "fabi.chillz@gmail.com");
        jsonObject.put("password", "12345qwert");
        accountData = jsonObject.toString();

        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("mail", "josh.romanowski@gmail.com");
        jsonObject2.put("password", "123456");
        newAccountData = jsonObject2.toString();
    }

    private static void changeAccount() {
        Form f = new Form();
        f.param("account", accountData);
        f.param("newAccount", newAccountData);
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(HOST).path("changeAccount");
        System.out.println(webTarget.getUri());
        Response response = webTarget.request().post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE),Response.class);
        System.out.println(response.readEntity(String.class));
        accountData = newAccountData;
        client.close();
    }

    private static void createAccount() {
        Form f = new Form();
        f.param("account", accountData);
        f.param("uuid", uuid);
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(HOST).path("createAccount");
        System.out.println(webTarget.getUri());
        Response response = webTarget.request().post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE),Response.class);
        System.out.println(response.readEntity(String.class));
        client.close();
    }

    private static void deleteAccount () {
        Form f = new Form();
        f.param("account", accountData);
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(HOST).path("deleteAccount");
        System.out.println(webTarget.getUri());
        Response response = webTarget.request().post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE),Response.class);
        System.out.println(response.readEntity(String.class));
        client.close();
    }

    private static void verifyAccount () {
        Form f = new Form();
        f.param("account", accountData);
        f.param("uuid", uuid);
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(HOST).path("verifyAccount");
        System.out.println(webTarget.getUri());
        Response response = webTarget.request().post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE),Response.class);
        System.out.println(response.readEntity(String.class));
        client.close();
    }

    private static void authenticate() {
        Form f = new Form();
        f.param("account", accountData);
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(HOST).path("authenticate");
        System.out.println(webTarget.getUri());
        Response response = webTarget.request().post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE),Response.class);
        System.out.println(response.readEntity(String.class));
        client.close();
    }

    private static void download() {
        Form f = new Form();
        f.param("account", accountData);
        f.param("videoId", "1");
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(HOST).path("videoDownload");
        Response response = webTarget.request().post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE), Response.class);
        InputStream inputStream = response.readEntity(InputStream.class);
        if (response.getStatus() == 200) {
            File downloadFile = new File("target" + File.separator + "downloaded.avi");
            try {
                Files.copy(inputStream, downloadFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                downloadFile.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        client.close();
    }


    private static void upload() {
        String vidName = "1487198226374";

        File videoFile = new File("src" + File.separator + "main" + File.separator + "resources" + File.separator + "VIDEO_" + vidName + ".mp4");
        File metadataFile = new File("src" + File.separator + "main" + File.separator + "resources" + File.separator + "META_" + vidName + ".json");
        File keyFile = new File("src" + File.separator + "main" + File.separator + "resources" + File.separator + "KEY_" + vidName + ".key");

        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(HOST).path("videoUpload").register(MultiPartFeature.class);
        MultiPart multiPart = new MultiPart();
        multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);

        FileDataBodyPart video = new FileDataBodyPart("video", videoFile, MediaType.APPLICATION_OCTET_STREAM_TYPE);
        FileDataBodyPart metadata = new FileDataBodyPart("metadata", metadataFile, MediaType.APPLICATION_OCTET_STREAM_TYPE);
        FileDataBodyPart key = new FileDataBodyPart("key", keyFile, MediaType.APPLICATION_OCTET_STREAM_TYPE);
        FormDataBodyPart data = new FormDataBodyPart("account", newAccountData);

        multiPart.bodyPart(video);
        multiPart.bodyPart(metadata);
        multiPart.bodyPart(key);
        multiPart.bodyPart(data);
        System.out.println(webTarget.getUri());
        Future<Response> futureResponse = webTarget.request().async().post(Entity.entity(multiPart, multiPart.getMediaType()), Response.class);
        try {
            Response response = futureResponse.get();
            System.out.println(response.readEntity(String.class));
        } catch (InterruptedException | ExecutionException | ProcessingException e) {
            e.printStackTrace();
        }
        client.close();
    }
}
