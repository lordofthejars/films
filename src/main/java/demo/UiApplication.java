package demo;

import java.io.InputStream;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import org.codehaus.plexus.util.StringOutputStream;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

@Path("/app")
public class UiApplication {

    @Context 
    HttpServletRequest servletRequest;
    @Context
    UriInfo uriInfo;

    @Inject
    UserService userService;

    @Inject
    MovieService moviesService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(InputStream object) throws JOSEException {
      JsonObject jsonObject = Json.createReader(object).readObject();

      JsonString username = (JsonString) jsonObject.get("username"); 
      JsonString password = (JsonString) jsonObject.get("password");

      if (authenticate(username.getString(), password.getString())) { 

        String token = createToken(username.getString(), "example.com"); 

        JsonObject responseDocument = Json.createObjectBuilder()
          .add("user", Json.createObjectBuilder().add("username", username).build())
          .add("token", token)
          .build(); 
        StringWriter content = new StringWriter();
        JsonWriter writer = Json.createWriter(content);
        writer.writeObject(responseDocument);
        return Response.ok(content.toString()).build();

      }

      return Response.status(Status.NOT_FOUND).build();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response movies() throws ParseException, JOSEException {
        String token = uriInfo.getQueryParameters().getFirst("xaccesstoken");
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(SharedSecret.getSecret());

        if(signedJWT.verify(verifier)) {
            List<JsonValue> movies = moviesService.findMovies();
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            for (JsonValue jsonValue : movies) {
                arrayBuilder.add(jsonValue);
            }
            JsonArray result = arrayBuilder.build();
            StringWriter content = new StringWriter();
            JsonWriter writer = Json.createWriter(content);
            writer.writeArray(result);
            return Response.ok(content.toString()).build();
        } else {
            return Response.status(404).build();
        }
    }

    private boolean authenticate(String username, String password) {
        return this.userService.findUser(username, password);
    }

    private String createToken(String subject, String issuer) throws JOSEException {

      JWSSigner signer = new MACSigner(SharedSecret.getSecret());

      JWTClaimsSet claimsSet = new JWTClaimsSet();
      claimsSet.setSubject(subject); 
      claimsSet.setIssueTime(new Date());
      claimsSet.setIssuer(issuer);
      //Not so secure but enough for an example
      claimsSet.setCustomClaim("ip", servletRequest.getRemoteAddr());

      SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
      signedJWT.sign(signer);

      return signedJWT.serialize(); 

    }


}
