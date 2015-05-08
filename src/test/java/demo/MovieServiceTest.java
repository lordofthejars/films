package demo;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonValue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class MovieServiceTest {

    @Deployment
    public static WebArchive deployment() {
        return ShrinkWrap
                .create(WebArchive.class)
                .addClass(MovieService.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsLibraries(
                        Maven.resolver()
                                .resolve("com.nimbusds:nimbus-jose-jwt:3.10",
                                        "org.glassfish:javax.json:1.0.4")
                                .withTransitivity().asFile());
    }

    @Inject
    MovieService movieService;

    @Test
    public void shouldReturnMovies() {
        List<JsonValue> findMovies = movieService.findMovies();
        assertThat(
                findMovies,
                hasItems(Json.createObjectBuilder()
                        .add("title", "Snow White and the Seven Dwarfs ")
                        .add("creator", "The brothers Grimm").add("front", "")
                        .build()));
    }
}
