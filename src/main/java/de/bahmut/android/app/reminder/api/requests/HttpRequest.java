package de.bahmut.android.app.reminder.api.requests;

import android.net.Uri;
import android.os.AsyncTask;
import lombok.Getter;
import lombok.Setter;

import de.bahmut.android.app.reminder.api.models.ApiResponse;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest extends AsyncTask<Uri, Void, ApiResponse> {

    private static final String DEFAULT_REQUEST_METHOD = "GET";
    private static final int READ_TIMEOUT = 15000;
    private static final int CONNECTION_TIMEOUT = 15000;

    @Getter
    @Setter
    private String requestMethod = DEFAULT_REQUEST_METHOD;

    @Getter
    private Map<String, String> headers = new HashMap<>();

    @Getter
    private Map<String, String> bodyParams = new HashMap<>();

    @Override
    protected ApiResponse doInBackground(final Uri... params) {
        final Uri requestUri = params[0];
        ApiResponse response;
        try {
            final HttpURLConnection connection = (HttpURLConnection) new URL(requestUri.toString()).openConnection();
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod(requestMethod);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setDoOutput(true);
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
            if (!bodyParams.isEmpty()) {
                try (
                        final OutputStream stream = connection.getOutputStream();
                        final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream, "UTF-8"))
                ) {
                    writer.write(getPostDataString(bodyParams));
                }
            }
            connection.connect();

            final String content;
            if (connection.getContentLength() > 0) {
                content = connection.getContent().toString();
            } else {
                content = null;
            }

            response = new ApiResponse(connection.getResponseCode(), content, connection.getContentType());
        } catch (final IOException e){
            response = null;
        }
        return response;
    }

    protected void onPostExecute(final ApiResponse result){
        super.onPostExecute(result);
    }

    private String getPostDataString(final Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first) {
                first = false;
            } else {
                result.append("&");
            }

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

}
