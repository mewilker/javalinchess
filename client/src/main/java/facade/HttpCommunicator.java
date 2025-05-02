package facade;

import chess.ChessPiece;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import response.Result;
import serialization.TypeAdapters;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpCommunicator {
    private final HttpClient http = HttpClient.newHttpClient();
    private final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Result.class, TypeAdapters.resultDeserializer())
            .registerTypeAdapter(ChessPiece.class, TypeAdapters.pieceDeserializer())
            .create();
    private int lastStatusCode;

    public Result doPost(String url, String token, Object request) throws ServerErrorException {
        String body = new Gson().toJson(request);
        try {
            HttpRequest httpReq = HttpRequest.newBuilder(new URI(url))
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .header("Content-Type", "application/json")
                    .header("authorization", token)
                    .build();

            HttpResponse<String> result = http.send(httpReq, HttpResponse.BodyHandlers.ofString());
            lastStatusCode = result.statusCode();
            return GSON.fromJson(result.body(), Result.class);
        }
        catch (URISyntaxException | IOException | InterruptedException e){
            throw new ServerErrorException("There was an issue contacting the server", e);
        }
    }

    public Result doDelete(String url, String token) throws ServerErrorException {
        try{
            HttpRequest request = HttpRequest.newBuilder(new URI(url))
                    .DELETE()
                    .header("authorization", token)
                    .build();

            HttpResponse<String> result = http.send(request, HttpResponse.BodyHandlers.ofString());
            lastStatusCode = result.statusCode();
            return GSON.fromJson(result.body(), Result.class);
        }
        catch (URISyntaxException | IOException | InterruptedException e){
            throw new ServerErrorException("There was an issue contacting the server", e);
        }
    }

    public Result doGet(String url, String token) throws ServerErrorException {
        try{
            HttpRequest request = HttpRequest.newBuilder(new URI(url))
                    .GET()
                    .header("authorization", token)
                    .build();
            HttpResponse<String> result = http.send(request, HttpResponse.BodyHandlers.ofString());
            lastStatusCode = result.statusCode();
            return GSON.fromJson(result.body(), Result.class);
        }
        catch (URISyntaxException | IOException | InterruptedException e){
            throw new ServerErrorException("There was an issue contacting the server", e);
        }
    }

    public int getLastStatusCode(){
        return lastStatusCode;
    }

}
