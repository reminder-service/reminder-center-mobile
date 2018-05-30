package de.bahmut.android.app.reminder.api.models;

public class ApiResponse {

    public final int code;
    public final String content;
    public final String contentType;

    public ApiResponse(
            final int code,
            final String content,
            final String contentType
    ) {
        this.code = code;
        this.content = content;
        this.contentType = contentType;
    }

}
