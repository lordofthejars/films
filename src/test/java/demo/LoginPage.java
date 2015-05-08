package demo;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Location("")
public class LoginPage {

    @FindBy(id = "username")
    private WebElement username;
    @FindBy(id = "password")
    private WebElement password;
    @FindBy(id = "loginBtn")
    private WebElement loginButton;

    public void login(String username, String password) {
        this.username.sendKeys(username);
        this.password.sendKeys(password);
        guardAjax(loginButton).click();
    }
}
