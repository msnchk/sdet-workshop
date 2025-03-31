package com.github.msnchk.tests;

import com.github.msnchk.helpers.CustomerDataProvider;
import com.github.msnchk.models.Customer;
import com.github.msnchk.helpers.Endpoint;
import com.github.msnchk.pages.AddCustomerPage;
import com.github.msnchk.pages.ManagerPage;
import io.qameta.allure.*;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * Класс {@code BankManagerUITest} содержит тесты для функционала управления клиентами
 */

@Epic("Bank Customer Management")
@Feature("UI Testing of Bank Manager Functionalities")
public class BankManagerUITest extends BaseTest {
    private static final Object lock = new Object();
    private ManagerPage managerPage;

    /**
     * Подготовка перед тестами.
     * Открывает страницу менеджера банка и ожидает её загрузки.
     *
     * @throws IllegalStateException если {@code WebDriver} не был инициализирован для текущего потока
     */
    @BeforeMethod()
    public void setUpTest() {
        WebDriver driver = getDriver();
        if (driver == null) {
            throw new IllegalStateException("WebDriver не был инициализирован для потока " + Thread.currentThread().getId());
        }
        synchronized (lock) {
            driver.get(Endpoint.BANK_MANAGER.getUrl());
        }
        managerPage = new ManagerPage(driver);
        managerPage.waitForPageLoaded(Endpoint.BANK_MANAGER.getUrl());
    }

    /**
     * Проверяет добавление клиента с корректными данными.
     * Ожидается, что появится Alert с подтверждением успешного создания клиента
     * и что его данные будут добавлены в список.
     */
    @Test(description = "Positive scenario: Adding a new customer")
    @Story("Add Customer")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldAddCustomerWithValidData() {
        SoftAssert softAssert = new SoftAssert();

        var addCustomerPage = managerPage.goToAddCustomer();
        addCustomerPage.waitForPageLoaded(Endpoint.ADD_CUSTOMER.getUrl());

        Customer customer = addCustomerPage.createCustomerWithGeneratedData();
        addCustomerPage
                .fillCustomerDataFields(customer.getFirstName(), customer.getLastName(), customer.getPostCode())
                .submitCustomerData();

        softAssert.assertTrue(addCustomerPage.isAlertWithTextPresent("Customer added successfully"),
                "Не получен ожидаемый алерт о создании пользователя");

        var customersPage = managerPage.goToCustomersList();
        customersPage.waitForPageLoaded(Endpoint.CUSTOMERS_LIST.getUrl());

        softAssert.assertTrue(customersPage.isCustomerPresent(
                        customer.getFirstName(), customer.getLastName(), customer.getPostCode()),
                "Клиент не найден в списке");

        softAssert.assertAll();
    }


    /**
     * Проверяет, что клиент не добавляется, если одно из полей пустое.
     * Ожидает отсутствия алерта и отсутствия клиента в списке.
     */
    @Test(description = "Negative scenario: Attempt to add a customer with empty fields", priority = 1, dataProvider = "invalidCustomerData", dataProviderClass = CustomerDataProvider.class)
    @Story("Add Customer")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldNotAddCustomerWithEmptyFields(Customer customer) {
        SoftAssert softAssert = new SoftAssert();

        var addCustomerPage = managerPage.goToAddCustomer();
        addCustomerPage.waitForPageLoaded(Endpoint.ADD_CUSTOMER.getUrl());

        addCustomerPage
                .fillCustomerDataFields(customer.getFirstName(), customer.getLastName(), customer.getPostCode())
                .submitCustomerData();

        softAssert.assertFalse(addCustomerPage.isAlertWithTextPresent("Customer added successfully"),
               "Получен алерт, появление которого не ожидалось");

        var customersPage = managerPage.goToCustomersList();
        customersPage.waitForPageLoaded(Endpoint.CUSTOMERS_LIST.getUrl());

        softAssert.assertFalse(customersPage.isCustomerPresent(
                        customer.getFirstName(), customer.getLastName(), customer.getPostCode()),
                "Клиент c некорректными данными найден в списке");

        softAssert.assertAll();
    }

    /**
     * Проверяет сортировку списка клиентов по имени.
     * Нажимает кнопку сортировки и ожидает, что клиенты будут сортированы в обратном порядке по алфавиту.
     * Нажимает еще раз, и ожидают что клиенты будут отсортированы по алфавиту.
     */
    @Test(description = "Checking sorting of customers by name")
    @Story("Sort Customers")
    @Severity(SeverityLevel.MINOR)
    public void shouldSortCustomersByName() {
        SoftAssert softAssert = new SoftAssert();

        var customersPage = managerPage.goToCustomersList();
        customersPage.waitForPageLoaded(Endpoint.CUSTOMERS_LIST.getUrl());

        customersPage.sortFirstNames();
        softAssert.assertTrue(customersPage.checkFirstNamesOrder(true));

        customersPage.sortFirstNames();
        softAssert.assertTrue(customersPage.checkFirstNamesOrder(false));

        softAssert.assertAll();
    }

    /**
     * Проверяет возможность удаления клиента.
     * Берется клиент с именем, наиболее близким по длине к средней длине имён в списке.
     * Ожидается, что клиент будет успешно удалён.
     */

    @Test(description = "Deleting a customer by name")
    @Story("Delete Customer")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldDeleteCustomerByName() {
        var customersPage = managerPage.goToCustomersList();
        customersPage.waitForPageLoaded(Endpoint.CUSTOMERS_LIST.getUrl());

        String nameToDelete = customersPage.getMiddleLengthName();
        Assert.assertFalse(nameToDelete.isEmpty(), "Не удалось определить имя для удаления");

        System.out.println("Удаляем клиента с именем: " + nameToDelete);
        Assert.assertTrue(customersPage.deleteName(nameToDelete),
                "Не удалось кликнуть по кнопке удаления клиента с именем " + nameToDelete);

        getDriver().navigate().refresh();
        customersPage.waitForPageLoaded(Endpoint.CUSTOMERS_LIST.getUrl());

        Assert.assertFalse(customersPage.getAllFirstNames().contains(nameToDelete),
                "Клиент с именем " + nameToDelete + " все еще присутствует в списке после удаления");
    }

}
