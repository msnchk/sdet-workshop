package com.github.msnchk.models;

/**
 * Класс {@code Customer} представляет собой модель клиента с полями:
 * имя, фамилия и почтовый индекс.
 */
public class Customer {
    private final String firstName;
    private final String lastName;
    private final String postCode;

    /**
     * Создаёт новый объект {@code Customer}.
     *
     * @param firstName имя клиента
     * @param lastName  фамилия клиента
     * @param postCode  почтовый индекс клиента
     */
    public Customer(String firstName, String lastName, String postCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.postCode = postCode;
    }

    /**
     * Возвращает имя клиента.
     *
     * @return имя клиента
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Возвращает фамилию клиента.
     *
     * @return фамилия клиента
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Возвращает почтовый индекс клиента.
     *
     * @return почтовый индекс клиента
     */
    public String getPostCode() {
        return postCode;
    }
}
