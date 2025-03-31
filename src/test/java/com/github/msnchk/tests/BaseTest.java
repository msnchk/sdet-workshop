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
    private static final Object lock = new Object();

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
                "--no-sandbox"
//                "--headless"
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

//
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//import org.testng.annotations.AfterMethod;
//import org.testng.annotations.BeforeMethod;
//
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentMap;
//
//public class BaseTest {
//    private static final ConcurrentMap<Long, WebDriver> driverMap = new ConcurrentHashMap<>();
//
//    @BeforeMethod(alwaysRun = true)
//    public void setup() {
//        ChromeOptions options = new ChromeOptions();
//
//        options.addArguments("--remote-allow-origins=*", "--disable-gpu", "--start-maximized");
//
//        WebDriver driver = new ChromeDriver(options);
//        long threadId = Thread.currentThread().getId();
//
//        System.out.println("Запуск setup() для потока " + threadId);
//
//        driverMap.put(threadId, driver);
//
//        driver.manage().deleteAllCookies();
//    }
//
//    @AfterMethod
//    public void tearDown() {
//        long threadId = Thread.currentThread().getId();
//
//        System.out.println("Выполняется tearDown() для потока " + threadId);
//
//        WebDriver driver = driverMap.remove(threadId);
//
//        if (driver != null) {
//            try {
//                driver.quit();
//            } catch (Exception e) {
//                System.out.println("Ошибка при закрытии драйвера: " + e.getMessage());
//            }
//        }
//    }
//
//    protected WebDriver getDriver() {
//        long threadId = Thread.currentThread().getId();
//
//        WebDriver driver = driverMap.get(Thread.currentThread().getId());
//        if (driver == null) {
//            throw new IllegalStateException("WebDriver не найден для потока " + Thread.currentThread().getId());
//        }
//        return driver;
//    }
//
//}
//
