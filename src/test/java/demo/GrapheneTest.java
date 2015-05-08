package demo;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.InitialPage;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(Arquillian.class)
public class GrapheneTest {

    private static final String WEBAPP_SRC = "src/main/webapp";

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        GenericArchive webAppDirectory = ShrinkWrap
                .create(GenericArchive.class).as(ExplodedImporter.class)
                .importDirectory(WEBAPP_SRC).as(GenericArchive.class);
       return ShrinkWrap
                .create(WebArchive.class, "films.war")
                .addPackages(true, "demo")
                .merge(webAppDirectory, "/",
                        Filters.include(".*\\.(js|css|html|xml)$"))
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsLibraries(
                        Maven.resolver()
                                .resolve("com.nimbusds:nimbus-jose-jwt:3.10",
                                        "org.glassfish:javax.json:1.0.4")
                                .withTransitivity().asFile());
    }

    @ArquillianResource
    URL contextPath;

    @Drone
    WebDriver driver;

    @Test
    public void shouldLogin(@InitialPage LoginPage loginPage) {
        loginPage.login("alex", "alex");
    }
}
