package com.github.msnchk.helpers;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Класс {@code DataGenerator} предназначен для генерации данных
 * (индекса, имени и фамилии) для теста, проверяющего функционал раздела Add Customer.
 */
public class DataGenerator {

    /**
     * Генерирует случайный почтовый индекс из 10 цифр поля Post Code.
     *
     * @return строка, представляющая почтовый индекс
     */
    public static String generatePostCode() {
        return String.valueOf(ThreadLocalRandom.current().nextLong(1_000_000_000L, 10_000_000_000L));
    }

    /**
     * Генерирует имя для поля First Name на основе почтового индекса.
     * Каждые две цифры индекса преобразуются в букву.
     *
     * @param postCode почтовый индекс, используемый для генерации имени
     * @return строка, представляющая сгенерированное имя
     */
    public static String generateFirstName(String postCode) {
        StringBuilder name = new StringBuilder();

        for (int i = 0; i < postCode.length(); i += 2) {
            int number = Integer.parseInt(postCode.substring(i, i + 2));
            char letter = (char) ((number % 26) + 'a');
            name.append(letter);
        }
        return name.toString();
    }

    /**
     * Генерирует случайную фамилию для поля Last Name длиной 7 символов,
     * состоящую из букв латинского алфавита.
     *
     * @return строка, представляющая фамилию
     */
    public static String generateLastName() {
        return ThreadLocalRandom.current()
                .ints('a', 'z' + 1)
                .limit(7)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
