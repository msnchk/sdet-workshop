package com.github.msnchk.tests;

import com.github.msnchk.helpers.PropertyProvider;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Класс {@code BaseTest} содержит базовую настройку для тестов.
 */
public class BaseTest {
    private final static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();
    private final String urlManagerPage = PropertyProvider.getInstance().getProperty("web.manager.url");

    /**
     * Выполняет настройку перед каждым тестом.
     * Создаёт новый экземпляр {@code WebDriver} и сохраняет его в {@code ThreadLocal}.
     */
    @BeforeMethod
    public void setup() {
        try {
            ChromeOptions options = new ChromeOptions();
            options.addArguments(
                    "--remote-allow-origins=*",
                    "--disable-gpu",
                    "--no-sandbox",
                    "--headless=new",
                    "--disable-dev-shm-usage",
                    "--window-size=1920,1080",
                    "--disable-blink-features=AutomationControlled"
            );
            WebDriver driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), options);
            tlDriver.set(driver);
            driver.manage().deleteAllCookies();
            driver.get(urlManagerPage);

        } catch (MalformedURLException e) {
            throw new RuntimeException("Ошибка при создании WebDriver: неверный URL Selenium Grid", e);
        }
    }

    /**
     * Закрывает браузер после выполнения теста
     */
    @AfterMethod
    public void tearDown() {
        WebDriver driver = getDriver();
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterClass
    public void cleanUp() {
        WebDriver driver = tlDriver.get();
        if (driver != null) {
            driver.quit();
        }
        tlDriver.remove();
    }

    /**
     * Возвращает текущий {@code WebDriver} для потока.
     *
     * @return экземпляр {@code WebDriver}
     */
    protected WebDriver getDriver() {
        return tlDriver.get();
    }
}