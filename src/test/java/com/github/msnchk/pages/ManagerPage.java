package com.github.msnchk.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Класс {@code ManagerPage} представляет страницу менеджера с меню,
 * содержащим переходы к управлению клиентами.
 */
public class ManagerPage extends BasePage<ManagerPage> {
    @FindBy(xpath = "//button[contains(text(), 'Add Customer')]")
    private WebElement addCustomerButton;

    @FindBy(xpath = "//button[contains(text(), 'Customers')]")
    private WebElement customersButton;

    /**
     * Конструктор страницы менеджера.
     *
     * @param driver экземпляр WebDriver
     */
    public ManagerPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Переход на страницу добавления клиента.
     *
     * @return объект {@code AddCustomerPage}
     */
    public AddCustomerPage goToAddCustomer() {
        addCustomerButton.click();
        return new AddCustomerPage(driver);
    }

    /**
     * Переход на страницу со списком клиентов.
     *
     * @return объект {@code CustomersPage}
     */
    public CustomersPage goToCustomersList() {
        customersButton.click();
        return new CustomersPage(driver);
    }
}
