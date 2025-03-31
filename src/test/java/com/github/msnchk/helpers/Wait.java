package com.github.msnchk.helpers;

import org.openqa.selenium.Alert;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

/**
 * Класс {@code Wait} предоставляет методы для явного ожидания в Selenium.
 */
public class Wait {
    private final WebDriverWait driverWait;

    /**
     * Приватный конструктор для инициализации объекта ожидания.
     *
     * @param driver  экземпляр WebDriver
     * @param timeout время ожидания в секундах
     */
    private Wait(WebDriver driver, long timeout) {
        this.driverWait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
    }

    /**
     * Создает объект {@code Wait} с таймаутом, заданным в properties.
     *
     * @param driver экземпляр WebDriver
     * @return объект {@code Wait}
     * @throws RuntimeException если значение таймаута некорректно
     */
    public static Wait of(WebDriver driver) {
        long timeout = getTimeoutFromProperties();
        return new Wait(driver, timeout);
    }

    /**
     * Извлекает значение таймаута из файла свойств.
     *
     * @return таймаут в секундах
     * @throws RuntimeException если значение некорректно
     */
    private static long getTimeoutFromProperties() {
        String timeoutStr = PropertyProvider.getInstance().getProperty("explicit.timeout");
        try {
            return Long.parseLong(timeoutStr);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Неверное значение таймаута в properties: " + timeoutStr, e);
        }
    }

    /**
     * Метод для ожидания, пока веб-элемент станет видимым.
     *
     * @param element веб-элемент
     */
    public void waitForElementToBeVisible(WebElement element) {
        driverWait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Ожидает, когда все элементы из списка станут видимыми.
     *
     * @param elements список веб-элементов
     */
    public void waitForAllElementsToBeVisible(List<WebElement> elements) {
        driverWait.until(ExpectedConditions.visibilityOfAllElements(elements));
    }

    /**
     * Ожидает загрузки указанного URL.
     *
     * @param url ожидаемый URL
     */
    public void waitForGetURL(String url) {
        driverWait.until(ExpectedConditions.urlToBe(url));
    }

    /**
     * Ожидает, пока элемент станет кликабельным.
     *
     * @param element веб-элемент
     * @return кликабельный элемент
     */
    public WebElement waitForElementToBeClickable(WebElement element) {
        return driverWait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Ожидает появления всплывающего окна (Alert).
     *
     * @return объект {@code Optional}, содержащий Alert, если он появился, или пустое значение, если истек таймаут
     */
    public Optional<Alert> waitForAlert() {
        try {
            return Optional.ofNullable(driverWait.until(ExpectedConditions.alertIsPresent()));
        } catch (TimeoutException e) {
            return Optional.empty();
        }
    }
}