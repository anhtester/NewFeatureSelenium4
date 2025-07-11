package com.anhtester;

import com.anhtester.common.BaseTest;
import com.google.common.util.concurrent.Uninterruptibles;
import org.openqa.selenium.By;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v136.network.Network;
import org.openqa.selenium.devtools.v136.network.model.Headers;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HandleAuthentication extends BaseTest {
    @Test
    public void handleAuthentication() {
        // Authentication username & password
        String url = "https://the-internet.herokuapp.com/basic_auth";
        String username = "admin";
        String password = "admin";

        // Get the devtools from the running driver and create a session
        DevTools devTools = ((HasDevTools) driver).getDevTools();
        devTools.createSession();

        // Enable the Network domain of devtools
        devTools.send(Network.enable(
                Optional.of(100000),
                Optional.of(100000),
                Optional.of(100000))
        );
        String auth = username + ":" + password;

        // Encoding the username and password using Base64 (java.util)
        String encodeToString = Base64.getEncoder().encodeToString(auth.getBytes());

        // Pass the network header -> Authorization : Basic <encoded String>
        Map<String, Object> headers = new HashMap<>();
        headers.put("Authorization", "Basic " + encodeToString);
        devTools.send(Network.setExtraHTTPHeaders(new Headers(headers)));

        // Load the application url
        driver.get(url);
        Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(3));

        String successFullyLoggedInText = driver.findElement(By.xpath("//p")).getText();
        System.out.println(successFullyLoggedInText);
    }
}
