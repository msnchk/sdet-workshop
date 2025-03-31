package com.github.msnchk.helpers;

import com.github.msnchk.models.Customer;
import org.testng.annotations.DataProvider;

/**
 * Класс {@code CustomerDataProvider} предоставляет тестовые данные
 * для негативных сценариев добавления клиентов.
 */
public class CustomerDataProvider {

    /**
     * Провайдер для тестов с некорректными данными клиента, когда одно из полей пустое.
     *
     * @return двумерный массив с объектами {@code Customer}
     */
    @DataProvider(name = "invalidCustomerData", parallel = true)
    public Object[][] invalidCustomerData() {
        return new Object[][]{
                {new Customer("", "LastName", "1234567890")},
                {new Customer("FirstName", "", "1234567890")},
                {new Customer("FirstName", "LastName", "")}
        };
    }
}
