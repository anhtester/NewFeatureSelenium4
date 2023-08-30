package com.anhtester;

import com.anhtester.common.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v114.log.Log;
import org.openqa.selenium.devtools.v114.network.Network;
import org.openqa.selenium.devtools.v114.network.model.ConnectionType;
import org.openqa.selenium.devtools.v114.security.Security;
import org.openqa.selenium.devtools.v115.emulation.Emulation;
import org.testng.annotations.Test;

import java.util.Optional;

public class HandleChromeDevTools extends BaseTest {

    @Test
    public void emulateSlowNetwork() {
        DevTools devTools = ((HasDevTools) driver).getDevTools();
        devTools.createSession();
        // Enable the Network domain of devtools
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        // Set 4G network
        devTools.send(Network.emulateNetworkConditions(
                false,
                500,
                2000000,
                3000000,
                Optional.of(ConnectionType.CELLULAR3G)));

        driver.get("https://cms.anhtester.com");
        WebUI.waitForPageLoaded();
        driver.findElement(By.xpath("//i[@class='la la-close fs-20']")).click();
        sleep(2);
    }

    @Test
    public void emulateGeolocation() {
        DevTools devTools = ((HasDevTools) driver).getDevTools();
        devTools.createSession();

        devTools.send(Emulation.setGeolocationOverride(
                Optional.of(52.5043),
                Optional.of(13.4501),
                Optional.of(1))
        );

        driver.get("https://where-am-i.org/");

        System.out.println("-----GeoLocation----");
        System.out.println("Location:" + driver.findElement(By.id("address")).getText());
        System.out.println("Latitude:" + driver.findElement(By.id("latitude")).getText());
        System.out.println("Longitude:" + driver.findElement(By.id("longitude")).getText());
        System.out.println("--------------------");
    }

    @Test
    public void viewConsoleLogs() {
        DevTools devTools = ((HasDevTools) driver).getDevTools();
        devTools.createSession();
        devTools.send(Log.enable());

        devTools.addListener(Log.entryAdded(), logEntry -> {
            System.out.println("-------------------------------------------");
            System.out.println("Request ID = " + logEntry.getNetworkRequestId());
            System.out.println("URL = " + logEntry.getUrl());
            System.out.println("Source = " + logEntry.getSource());
            System.out.println("Level = " + logEntry.getLevel());
            System.out.println("Text = " + logEntry.getText());
            System.out.println("Timestamp = " + logEntry.getTimestamp());
            System.out.println("-------------------------------------------");
        });

        driver.get("https://anhtester.com/abc");
    }

    @Test
    public void checkSecuritySite() {
        DevTools devTools = ((HasDevTools) driver).getDevTools();
        devTools.createSession();
        devTools.send(Security.enable());
        devTools.send(Security.setIgnoreCertificateErrors(true));

        driver.get("https://anhtester.com");
    }
}
