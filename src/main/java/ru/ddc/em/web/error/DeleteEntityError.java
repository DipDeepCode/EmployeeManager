package ru.ddc.em.web.error;

public class DeleteEntityError extends Exception {

    public DeleteEntityError() {
        super();
    }

    public DeleteEntityError(String message) {
        super(message);
    }
}
