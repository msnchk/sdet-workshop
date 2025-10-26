package com.github.msnchk.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Класс {@code PropertyProvider} отвечает за загрузку и предоставление
 * свойств из файла конфигурации.
 */
public class PropertyProvider {
    private static final String PROPERTIES_FILE = "env_local.properties";
    private static final PropertyProvider INSTANCE = new PropertyProvider();
    private final Properties properties = new Properties();

    /**
     * Приватный конструктор загружает свойства из файла.
     * Использует {@code ClassLoader} для поиска файла в ресурсах.
     *
     * @throws RuntimeException если файл не найден или произошла ошибка при чтении
     */
    private PropertyProvider() {
        try (InputStream propertiesInputStream = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (propertiesInputStream == null) {
                throw new RuntimeException("Properties file not found: " + PROPERTIES_FILE);
            }
            properties.load(propertiesInputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file", e);
        }
    }

    /**
     * Метод для получения единственного экземпляра класса.
     *
     * @return экземпляр {@code PropertyProvider}
     */
    public static PropertyProvider getInstance() {
        return INSTANCE;
    }

    /**
     * Метод для получения значения свойства по ключу.
     *
     * @param key ключ свойства
     * @return значение свойства
     * @throws IllegalArgumentException если ключ отсутствует или значение пустое
     */
    public String getProperty(String key) {
        synchronized (properties) {
            if (!properties.containsKey(key)) {
                throw new IllegalArgumentException("Ключ '" + key + "' отсутствует в properties");
            }
            String value = properties.getProperty(key);
            if (value == null || value.isEmpty()) {
                throw new IllegalArgumentException("Значение для ключа '" + key + "' отсутствует или пусто");
            }
            return value;
        }
    }
}

