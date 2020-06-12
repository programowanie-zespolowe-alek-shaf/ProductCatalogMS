package pl.agh.product.catalog.common.util;

public enum FieldName {

    TITLE("title"),
    AUTHOR("author"),
    CATEGORY("category"),
    PRICE("price"),
    AVAILABLE("available"),
    RECOMMENDED("recommended"),
    COVER_TYPE("coverType");

    private final String name;

    FieldName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
