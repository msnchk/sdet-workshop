package com.github.msnchk.tests;

import com.github.msnchk.helpers.CustomerDataProvider;
import com.github.msnchk.helpers.PropertyProvider;
import com.github.msnchk.models.Customer;
import com.github.msnchk.pages.ManagerPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * Класс {@code BankManagerUITest} содержит тесты для функционала управления клиентами
 */

@Epic("Bank Customer Management")
@Feature("UI Testing of Bank Manager Functionalities")
public class BankManagerUITest extends BaseTest {
    private final String urlManagerPage = PropertyProvider.getInstance().getProperty("web.manager.url");
    private final String urlAddCustomer = PropertyProvider.getInstance().getProperty("web.addcust.url");
    private final String urlCustomersList = PropertyProvider.getInstance().getProperty("web.customers.url");

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
        ManagerPage managerPage = new ManagerPage(getDriver());
        managerPage.waitForPageLoaded(urlManagerPage);
        var addCustomerPage = managerPage.goToAddCustomer();
        addCustomerPage.waitForPageLoaded(urlAddCustomer);
        Customer customer = addCustomerPage.createCustomerWithGeneratedData();
        addCustomerPage
                .fillCustomerDataFields(customer.firstName(), customer.lastName(), customer.postCode())
                .submitCustomerData();

        softAssert.assertTrue(addCustomerPage.isAlertWithTextPresent("Customer added successfully"),
                "Не получен ожидаемый алерт о создании пользователя");

        var customersPage = managerPage.goToCustomersList();
        customersPage.waitForPageLoaded(urlCustomersList);

        softAssert.assertTrue(customersPage.isCustomerPresent(
                        customer.firstName(), customer.lastName(), customer.postCode()),
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
        ManagerPage managerPage = new ManagerPage(getDriver());
        managerPage.waitForPageLoaded(urlManagerPage);
        var addCustomerPage = managerPage.goToAddCustomer();
        addCustomerPage.waitForPageLoaded(urlAddCustomer);
        addCustomerPage
                .fillCustomerDataFields(customer.firstName(), customer.lastName(), customer.postCode())
                .submitCustomerData();

        softAssert.assertFalse(addCustomerPage.isAlertWithTextPresent("Customer added successfully"),
               "Получен алерт, появление которого не ожидалось");

        var customersPage = managerPage.goToCustomersList();
        customersPage.waitForPageLoaded(urlCustomersList);

        softAssert.assertFalse(customersPage.isCustomerPresent(
                        customer.firstName(), customer.lastName(), customer.postCode()),
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
        ManagerPage managerPage = new ManagerPage(getDriver());
        managerPage.waitForPageLoaded(urlManagerPage);
        var customersPage = managerPage.goToCustomersList();
        customersPage.waitForPageLoaded(urlCustomersList);
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
        ManagerPage managerPage = new ManagerPage(getDriver());
        managerPage.waitForPageLoaded(urlManagerPage);
        var customersPage = managerPage.goToCustomersList();
        customersPage.waitForPageLoaded(urlCustomersList);
        String nameToDelete = customersPage.getMiddleLengthName();

        Assert.assertFalse(nameToDelete.isEmpty(), "Не удалось определить имя для удаления");
        System.out.println("Удаляем клиента с именем: " + nameToDelete);
        Assert.assertTrue(customersPage.deleteName(nameToDelete),
                "Не удалось кликнуть по кнопке удаления клиента с именем " + nameToDelete);

        getDriver().navigate().refresh();
        customersPage.waitForPageLoaded(urlCustomersList);

        Assert.assertFalse(customersPage.getAllFirstNames().contains(nameToDelete),
                "Клиент с именем " + nameToDelete + " все еще присутствует в списке после удаления");
    }
}
