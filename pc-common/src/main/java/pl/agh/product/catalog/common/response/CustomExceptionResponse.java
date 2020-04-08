package pl.agh.product.catalog.common.response;

public class CustomExceptionResponse {

    private String error;

    public CustomExceptionResponse() {

    }

    public CustomExceptionResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}