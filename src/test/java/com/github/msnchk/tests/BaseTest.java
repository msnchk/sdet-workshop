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
    private final String localhost = PropertyProvider.getInstance().getProperty("web.selenium.url");
    protected final String managerUrl = PropertyProvider.getInstance().getProperty("web.manager.url");

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
            WebDriver driver = new RemoteWebDriver(new URL(localhost), options);
            tlDriver.set(driver);
            driver.manage().deleteAllCookies();
            driver.get(managerUrl);

        } catch (MalformedURLException e) {
            throw new RuntimeException("Ошибка при создании WebDriver: неверный URL Selenium Grid", e);
        }
    }

    /**
     * Завершает работу WebDriver после каждого теста.
     * Закрывает браузер и освобождает ресурсы.
     */
    @AfterMethod
    public void tearDown() {
        WebDriver driver = getDriver();
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Очищает WebDriver после выполнения всех тестов класса.
     * Завершает работу WebDriver и удаляет его из ThreadLocal.
     */
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