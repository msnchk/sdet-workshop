package com.github.msnchk.pages;

import com.github.msnchk.helpers.Wait;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Базовый класс для всех страниц, содержит общие методы взаимодействия с элементами.
 */
public class BasePage {
    protected final WebDriver driver;
    protected final Wait wait;

    @FindBy(className = "mainHeading")
    protected WebElement mainHeader;

    /**
     * Конструктор базовой страницы.
     *
     * @param driver экземпляр WebDriver
     */
    public BasePage(final WebDriver driver) {
        this.driver = driver;
        this.wait = Wait.of(driver);
        PageFactory.initElements(driver, this);
    }

    /**
     * Дожидается доступности элемента и выполняет клик.
     *
     * @param element веб-элемент, по которому нужно кликнуть
     */
    protected void performClick(final WebElement element) {
        wait.waitForElementToBeClickable(element).click();
    }

    /**
     * Вводит текст в поле.
     *
     * @param element веб-элемент, в который вводится текст
     * @param text    текст для ввода
     */
    protected void enterText(final WebElement element, final String text) {
        WebElement inputField = wait.waitForElementToBeClickable(element);
        inputField.clear();
        inputField.sendKeys(text);
    }

    /**
     * Ожидает загрузку страницы по URL и появление заголовка страницы.
     *
     * @param url ожидаемый URL страницы
     */

    public void waitForPageLoaded(String url) {
        wait.waitForGetURL(url);
        wait.waitForElementToBeVisible(mainHeader);
    }
}
