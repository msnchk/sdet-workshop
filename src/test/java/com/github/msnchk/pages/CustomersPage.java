package com.github.msnchk.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс {@code CustomersPage} представляет страницу со списком клиентов.
 */
public class CustomersPage extends BasePage {
    @FindBy(xpath = "//tr[@class='ng-scope']/td[1]")
    List<WebElement> firstNameColumn;

    @FindBy(xpath = "//a[contains(@ng-click, \"sortType = 'fName'\")]")
    WebElement firstNameSort;

    /**
     * Конструктор страницы клиентов.
     *
     * @param driver экземпляр WebDriver
     */
    public CustomersPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Проверяет, существует ли клиент с указанными именем, фамилией и почтовым кодом.
     *
     * @param firstName имя клиента
     * @param lastName фамилия клиента
     * @param postCode почтовый код клиента
     * @return {@code true}, если клиент найден, иначе {@code false}
     */
    @Step("Checking if customer exists: First Name = {firstName}, Last Name = {lastName}, Post Code = {postCode}")
    public boolean isCustomerPresent(String firstName, String lastName, String postCode) {
        String xpath = String.format("//tr[td[text()='%s'] and td[text()='%s'] and td[text()='%s']]",
                firstName, lastName, postCode);
        try {
            return driver.findElement(By.xpath(xpath)).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Удаляет клиента по имени.
     *
     * @param name имя клиента, которого нужно удалить
     * @return {@code true}, если удаление прошло успешно, иначе {@code false}
     */
    @Step("Deleting customer with name: {name}")
    public boolean deleteName(String name) {
        if (name.isEmpty()) {
            return false;
        }
        String xpath = "//tr[td[text()='" + name + "']]//button[contains(@ng-click, 'deleteCust')]";

        try {
            WebElement deleteButton = driver.findElement(By.xpath(xpath));
            deleteButton.click();
            return true;
        } catch (NoSuchElementException e) {
            System.out.println("Кнопка удаления не найдена для имени: " + name);
            return false;
        }
    }

    /**
     * Проверяет, отсортированы ли имена клиентов в заданном порядке.
     *
     * @param reversed {@code true}, если нужно проверить обратный порядок, иначе {@code false}
     * @return {@code true}, если список отсортирован корректно, иначе {@code false}
     */
    @Step("Checking if customer names are sorted. Reverse order: {reversed}")
    public boolean checkFirstNamesOrder(boolean reversed) {
        wait.waitForAllElementsToBeVisible(firstNameColumn);

        List<String> names = getAllFirstNames();

        List<String> sortedNames = names.stream()
                .sorted(reversed ? String.CASE_INSENSITIVE_ORDER.reversed() : String.CASE_INSENSITIVE_ORDER)
                .toList();

        return names.equals(sortedNames);
    }

    /**
     * Находит имя клиента, длина которого ближе всего к средней длине всех имён в списке.
     * Если таких несколько, берет первое подходящее.
     *
     * @return имя клиента со средней длиной или пустая строка, если список пуст
     */
    @Step("Finding customer name closest to the average length")
    public String getMiddleLengthName() {
        List<String> names = getAllFirstNames();
        if (names.isEmpty()) {
            return "";
        }

        double average = names.stream()
                .mapToInt(String::length)
                .average()
                .orElse(0);

        int roundedAverage = (int) Math.round(average);

        return names.stream()
                .min(Comparator.comparingInt(name -> Math.abs(name.length() - roundedAverage)))
                .orElse("");
    }

    /**
     * Получает список всех имён клиентов со страницы.
     *
     * @return список имён
     */
    @Step("Retrieving all customer first names")
    public List<String> getAllFirstNames() {
        return firstNameColumn.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    /**
     * Выполняет сортировку имён клиентов.
     * Два клика для сортировки в порядке возрастания.
     */
    @Step("Press button to sort customer names by first name")
    public void sortFirstNames() {
        firstNameSort.click();
    }
}
