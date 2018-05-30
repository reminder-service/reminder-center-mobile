package de.bahmut.android.app.reminder.api;

import android.net.Uri;
import de.bahmut.android.app.reminder.api.models.ApiResponse;
import de.bahmut.android.app.reminder.api.requests.HttpRequest;

import android.util.Base64;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class ReminderClient {

    private static final int CODE_CREATED = 201;
    private static final String IDENTIFIER = "identifier";
    private static final String MESSAGE = "message";

    private final Uri baseUri;
    private final String username;
    private final String password;

    public ReminderClient(
            final Uri baseUri,
            final String username,
            final String password
    ) {
        this.baseUri = baseUri;
        this.username = username;
        this.password = password;
    }

    public void sendMessage(final String message) throws ReminderException {
        final HttpRequest request = new HttpRequest();
        request.setRequestMethod("POST");
        request.getHeaders().putAll(createAuthHeader());
        request.getBodyParams().put(MESSAGE, message);

        final Uri requestUri = baseUri.buildUpon()
                .appendPath(MESSAGE)
                .build();

        final ApiResponse response;
        try {
            response = request.execute(requestUri).get();
        } catch (final Exception e) {
            throw new ReminderException("Unexpected Exception: " + e.getClass().getName() + " - " + e.getMessage());
        }

        if (response == null) {
            throw new ReminderException("Could not reach server");
        }

        if (response.code != CODE_CREATED) {
            throw new ReminderException("Message could not be send: Error Code = " + response.code + "; Message = " + response.content);
        }
    }

    private Map<String, String> createAuthHeader() {
        Map<String, String> header = new HashMap<>();
        final String authentication = "Basic " + Base64.encodeToString((username + ":" + password).getBytes(), Base64.DEFAULT);
        header.put("Authorization", authentication);
        return header;
    }

}
