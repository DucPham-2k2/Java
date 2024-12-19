package com.ducpham.exception;

public enum EnumException {

    CREATE_FAILED(401, "Create failed: "),
    UPDATE_FAILED(402, "Update failed: "),
    DELETE_FAILED(403, "Delete failed: "),
    NOT_FOUND(404, "Not found: "),
    EXISTED_DATA(405, "Exist data: "),
    SQL_EXCEPTION(406, "SQL exception: ");

    private final int code;
    private final String message;

    EnumException(int code, String message) {

        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
