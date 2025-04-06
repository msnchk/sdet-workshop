package com.github.msnchk.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Класс {@code Customer} представляет модель клиента с полями:
 * имя, фамилия и почтовый индекс.
 * Генерация геттеров и конструктора осуществляется с помощью Lombok.
 */
@Getter
@AllArgsConstructor
public class Customer {
    private String firstName;
    private String lastName;
    private String postCode;
}
