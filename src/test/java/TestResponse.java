import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v85.network.Network;
import org.openqa.selenium.devtools.v85.network.model.Request;
import org.openqa.selenium.devtools.v85.network.model.RequestId;
import org.openqa.selenium.devtools.v85.network.model.Response;
import org.testng.annotations.Test;

import java.util.Optional;

public class TestResponse {

    ChromeDriver driver = new ChromeDriver();

    @Test
    public void firstTest(){
        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty()
                , Optional.empty()));
        devTools.addListener(Network.requestWillBeSent(), requestConsumer -> {
            Request request = requestConsumer.getRequest();
//            System.out.println(request.getUrl());
        });
        RequestId[] requestIds = new RequestId[1];

        devTools.addListener(Network.responseReceived(), responseConsumer ->{
            Response response = responseConsumer.getResponse();
            requestIds[0] = responseConsumer.getRequestId();
            if(response.getUrl().contains("ws_api.php")){
                System.out.println(response.getUrl()+ ":" +response.getStatus());
                String responseBody = devTools.send(Network.getResponseBody(requestIds[0])).getBody();
                System.out.println(responseBody);
            }
        });

        driver.navigate().to("https://weatherstack.com/");

    }
}