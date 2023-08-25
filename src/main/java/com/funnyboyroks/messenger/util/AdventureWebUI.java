package com.funnyboyroks.messenger.util;

import com.funnyboyroks.messenger.Messenger;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class AdventureWebUI {

    private static final Gson       GSON   = new GsonBuilder().create();
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final URI        ROOT;

    static {
        try {
            ROOT = new URI("https://webui.advntr.dev");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static CompletableFuture<String> createEditor(String input, String command) {
        String json = GSON.toJson(new InputRequestPayload(input, command, Messenger.instance().getDescription().getName()));

        URI uri = ROOT.resolve(URI.create("/api/editor/input"));
        HttpRequest request = HttpRequest.newBuilder()
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .uri(uri)
            .build();

        if (Messenger.config().debugLogging) {
            Messenger.logger().info("Creating editor at " + uri);
        }

        return CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(res -> {
                if (res.statusCode() != 200) {
                    throw new RuntimeException("The server could not handle the request.  Status Code: " + res.statusCode());
                } else {
                    try {
                        String body = res.body();
                        InputResponsePayload payload = GSON.fromJson(body, InputResponsePayload.class);
                        if (payload == null) {
                            throw new RuntimeException("Unexpected response from server.  Body: " + body);
                        }
                        return payload.token();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }

                }
            });
    }

    public static CompletableFuture<String> getOutput(String token) {
        Preconditions.checkArgument(!token.matches("(?i)^[a-f0-9]$"), "Invalid token provided: " + token);

        URI uri = ROOT.resolve(URI.create("/api/editor/output?token=" + token));
        HttpRequest request = HttpRequest.newBuilder()
            .GET()
            .uri(uri)
            .build();

        if (Messenger.config().debugLogging) {
            Messenger.logger().info("Fetching output from " + uri);
        }

        return CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(res -> {
                if (res.statusCode() == 404) {
                    if (Messenger.config().debugLogging) {
                        Messenger.logger().info("Output is null");
                    }
                    return null;
                } else if (res.statusCode() != 200) {
                    throw new RuntimeException("The server could not handle the request. Status code: " + res.statusCode());
                } else {
                    return res.body();
                }
            });
    }


    private record InputRequestPayload(String input, String command, String application) {
        // Intentionally Empty
    }

    private record InputResponsePayload(String token) {
        // Intentionally Empty
    }

}
