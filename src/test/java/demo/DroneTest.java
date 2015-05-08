//Added
package demo;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
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
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

@RunWith(Arquillian.class)
public class DroneTest {

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
    public void shouldShowLoginPage() {
        driver.get(contextPath.toExternalForm());
        driver.findElement(By.id("username")).sendKeys("alex");
        driver.findElement(By.id("password")).sendKeys("alex");
        driver.findElement(By.id("loginBtn")).click();
        //wait
    }
}
// End Added