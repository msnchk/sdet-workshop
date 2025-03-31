package com.github.msnchk.helpers;

/**
 * Перечисление {@code Endpoint} содержит пути для различных страниц веб-приложения:
 * основное меню менеджера банка, добавление клиента, список клиентов.
 */
public enum Endpoint {

    BANK_MANAGER(""),
    ADD_CUSTOMER("/addCust"),
    CUSTOMERS_LIST("/list");

    private final String path;
    private String url;

    /**
     * Конструктор, задающий путь к ресурсу.
     *
     * @param path относительный путь
     */
    Endpoint(String path) {
        this.path = path;
    }

    /**
     * Метод для получения полного URL эндпоинта, использующий значения из
     * файла конфигурации через PropertyProvider.
     *
     * @return строка с полным URL
     */
    public String getUrl() {
        if (url == null) {
            synchronized (this) {
                if (url == null) {
                    String managerPath = PropertyProvider.getInstance().getProperty("web.url")
                            .concat(PropertyProvider.getInstance().getProperty("web.manager.path"));
                    url = managerPath.concat(path);
                }
            }
        }
        return url;
    }
}

