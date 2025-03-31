package com.github.msnchk.tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Класс {@code BaseTest} содержит базовую настройку для тестов.
 */
public class BaseTest {
    private static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

    /**
     * Выполняет настройку перед каждым тестом.
     * Создаёт новый экземпляр {@code WebDriver} и сохраняет его в {@code ThreadLocal}.
     */
    @BeforeMethod
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments(
                "--remote-allow-origins=*",
                "--disable-gpu",
                "--no-sandbox",
                "--headless"
        );
        final WebDriver driver = new ChromeDriver(options);
        tlDriver.set(driver);
        driver.manage().deleteAllCookies();
    }

    /**
     * Закрывает браузер после выполнения теста и удаляет драйвер из {@code ThreadLocal}.
     */
    @AfterMethod
    public void tearDown() {
        try {
            WebDriver driver = tlDriver.get();
            if (driver != null) {
                try {
                    driver.quit();
                } catch (Exception e) {
                    System.out.println("Ошибка при закрытии драйвера: " + e.getMessage());
                }
            }
        } finally {
            tlDriver.remove();
        }
    }

    /**
     * Возвращает текущий {@code WebDriver} для потока.
     *
     * @return экземпляр {@code WebDriver} или {@code null}, если драйвер не был инициализирован
     */
    protected final WebDriver getDriver() {
        return tlDriver.get();
    }
}