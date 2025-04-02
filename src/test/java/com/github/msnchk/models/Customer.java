package com.github.msnchk.models;

/**
 * Класс {@code Customer} представляет собой модель клиента с полями:
 * имя, фамилия и почтовый индекс.
 */
public record Customer(String firstName, String lastName, String postCode) {}
