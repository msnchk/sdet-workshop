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
    private final String addCustUrl = PropertyProvider.getInstance().getProperty("web.addcust.url");
    private final String customersUrl = PropertyProvider.getInstance().getProperty("web.customers.url");

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
        var addCustomerPage = managerPage
                .waitForPageLoaded(managerUrl)
                .goToAddCustomer();
        Customer customer = addCustomerPage
                .waitForPageLoaded(addCustUrl)
                .createCustomerWithGeneratedData();
        addCustomerPage
                .fillCustomerDataFields(customer.getFirstName(), customer.getLastName(), customer.getPostCode())
                .submitCustomerData();

        softAssert.assertTrue(addCustomerPage.isAlertWithTextPresent("Customer added successfully"),
                "Не получен ожидаемый алерт о создании пользователя");
        var customersPage = managerPage
                .goToCustomersList()
                .waitForPageLoaded(customersUrl);

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
        ManagerPage managerPage = new ManagerPage(getDriver());
        var addCustomerPage = managerPage
                .waitForPageLoaded(managerUrl)
                .goToAddCustomer();
        addCustomerPage
                .waitForPageLoaded(addCustUrl)
                .fillCustomerDataFields(customer.getFirstName(), customer.getLastName(), customer.getPostCode())
                .submitCustomerData();

        softAssert.assertFalse(addCustomerPage.isAlertWithTextPresent("Customer added successfully"),
               "Получен алерт, появление которого не ожидалось");
        var customersPage = managerPage
                .goToCustomersList()
                .waitForPageLoaded(customersUrl);

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
        ManagerPage managerPage = new ManagerPage(getDriver());
        var customersPage = managerPage
                .waitForPageLoaded(managerUrl)
                .goToCustomersList();
        customersPage
                .waitForPageLoaded(customersUrl)
                .sortFirstNames();

        softAssert.assertTrue(customersPage.checkFirstNamesOrder(true),
                "Ошибка: имена клиентов не отсортированы по убыванию.");
        customersPage.sortFirstNames();

        softAssert.assertTrue(customersPage.checkFirstNamesOrder(false),
                "Ошибка: имена клиентов не отсортированы по возрастанию.");
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
        var customersPage = managerPage
                .waitForPageLoaded(managerUrl)
                .goToCustomersList();
        String nameToDelete = customersPage
                .waitForPageLoaded(customersUrl)
                .getMiddleLengthName();

        Assert.assertFalse(nameToDelete.isEmpty(), "Не удалось определить имя для удаления");
        Assert.assertTrue(customersPage.deleteName(nameToDelete),
                "Не удалось кликнуть по кнопке удаления клиента с именем " + nameToDelete);
        customersPage.waitForPageLoaded(customersUrl);
        Assert.assertFalse(customersPage.getAllFirstNames().contains(nameToDelete),
                "Клиент с именем " + nameToDelete + " все еще присутствует в списке после удаления");
    }
}
