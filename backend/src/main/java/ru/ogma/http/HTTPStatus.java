package ru.ogma.http;

public enum HTTPStatus {
    OK(200),
    CREATED(201),
    NO_CONTENT(204),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    CONFLICT(409),
    UNPROCESSABLE_CONTENT(422),
    INTERNAL_SERVER_ERROR(500);

    private final int code;

    HTTPStatus(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }
}
