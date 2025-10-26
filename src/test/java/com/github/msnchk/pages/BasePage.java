package com.github.msnchk.pages;

import com.github.msnchk.helpers.Wait;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Базовый класс для всех страниц, содержит общие методы взаимодействия с элементами.
 */
public abstract class BasePage<T extends BasePage<T>>  {
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
     * Ожидает загрузку страницы, проверяя соответствие ожидаемого URL и видимость заголовка.
     *
     * @param url ожидаемый URL страницы
     * @return текущий объект страницы после завершения ожидания
     */
    @SuppressWarnings("unchecked")
    public T waitForPageLoaded(String url) {
        wait.waitForGetURL(url);
        wait.waitForElementToBeVisible(mainHeader);
        return (T) this;
    }
}
