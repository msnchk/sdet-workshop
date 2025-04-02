package com.github.msnchk.pages;

import com.github.msnchk.models.Customer;
import com.github.msnchk.helpers.DataGenerator;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Класс {@code AddCustomerPage} представляет страницу добавления нового клиента.
 */
public class AddCustomerPage extends BasePage {

    @FindBy(xpath = "//input[@ng-model='fName']")
    private WebElement firstNameInput;

    @FindBy(xpath = "//input[@ng-model='lName']")
    private WebElement lastNameInput;

    @FindBy(xpath = "//input[@ng-model='postCd']")
    private WebElement postCodeInput;

    @FindBy(xpath = "//button[text()='Add Customer']")
    private WebElement addButton;

    /**
     * Конструктор страницы добавления клиента.
     *
     * @param driver экземпляр {@code WebDriver}
     */
    public AddCustomerPage(final WebDriver driver) {
        super(driver);
    }

    /**
     * Заполняет форму данными клиента.
     *
     * @param firstName имя клиента
     * @param lastName  фамилия клиента
     * @param postCode  почтовый индекс клиента
     * @return текущий объект {@code AddCustomerPage}
     */

    @Step("Filling in customer details: First Name = {firstName}, Last Name = {lastName}, Post Code = {postCode}")
    public AddCustomerPage fillCustomerDataFields(String firstName, String lastName, String postCode) {
        postCodeInput.sendKeys(postCode);
        firstNameInput.sendKeys(firstName);
        lastNameInput.sendKeys(lastName);
        return this;
    }

    /**
     * Создает клиента со случайно сгенерированными данными.
     *
     * @return объект {@code Customer} с заполненными данными
     */
    @Step("Generating a customer with random data")
    public Customer createCustomerWithGeneratedData() {
        String postCode = DataGenerator.generatePostCode();
        String firstName = DataGenerator.generateFirstName(postCode);
        String lastName = DataGenerator.generateLastName();

        return new Customer(firstName, lastName, postCode);
    }

    /**
     * Нажимает кнопку "Add Customer" для отправки формы.
     */
    @Step("Submitting the customer data")
    public void submitCustomerData() {
        addButton.click();
    }

    /**
     * Проверяет, появился ли всплывающий alert с ожидаемым текстом.
     *
     * @param expectedText ожидаемый текст во всплывающем окне
     * @return {@code true}, если alert содержит ожидаемый текст, иначе {@code false}
     */
    @Step("Checking if an alert with expected text '{expectedText}' is displayed")
    public boolean isAlertWithTextPresent(String expectedText) {
        return wait.waitForAlert()
                .map(alert -> {
                    boolean containsText = alert.getText().contains(expectedText);
                    alert.accept();
                    return containsText;
                })
                .orElse(false);
    }
}
