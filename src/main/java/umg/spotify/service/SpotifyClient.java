package umg.spotify.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import com.fasterxml.jackson.databind.JsonNode;
import umg.spotify.model.Track;

import java.util.Base64;
import java.util.Optional;

@Component
public class SpotifyClient {
    private final RestTemplate restTemplate;
    private String accessToken; // Guardar el token para reutilizarlo

    @Autowired
    public SpotifyClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Método para obtener información de la pista por ISRC
    public Optional<Track> fetchTrackByIsrc(String isrc) {
        ensureValidAccessToken(); // Asegura que el token esté actualizado
        String url = "https://api.spotify.com/v1/search?q=isrc:" + isrc + "&type=track";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, request, JsonNode.class);

            if (response.getBody() != null && response.getBody().has("tracks") && 
                response.getBody().get("tracks").has("items") && 
                response.getBody().get("tracks").get("items").size() > 0) {

                JsonNode trackNode = response.getBody().get("tracks").get("items").get(0);
                return Optional.of(new Track(
                        isrc,
                        trackNode.get("name").asText(),
                        trackNode.get("artists").get(0).get("name").asText(),
                        trackNode.get("album").get("name").asText(),
                        trackNode.get("album").get("id").asText(),
                        trackNode.get("explicit").asBoolean(),
                        trackNode.get("duration_ms").asInt() / 1000,
                        fetchAlbumCover(trackNode.get("album").get("id").asText()).orElse("")
                ));
            }
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // Método para obtener la portada del álbum
    public Optional<String> fetchAlbumCover(String albumId) {
        ensureValidAccessToken();
        String url = "https://api.spotify.com/v1/albums/" + albumId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, request, JsonNode.class);

            if (response.getBody() != null && response.getBody().has("images") &&
                response.getBody().get("images").size() > 0) {
                return Optional.of(response.getBody().get("images").get(0).get("url").asText());
            }
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // Método para descargar la imagen de portada
    public byte[] downloadCover(String url) {
        try {
            return restTemplate.getForObject(url, byte[].class);
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    // Obtener el token de Spotify
    private String getSpotifyAccessToken() {
        String clientId = "8f163a0480ef4dce9c0a2ae80eb6b085";
        String clientSecret = "42f25229d2e9498e96c5912065b555ca";
        String tokenUrl = "https://accounts.spotify.com/api/token";

        String authHeader = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + authHeader);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> request = new HttpEntity<>("grant_type=client_credentials", headers);

        ResponseEntity<JsonNode> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, JsonNode.class);

        return response.getBody().get("access_token").asText();
    }

    // Asegurar que el token de acceso es válido antes de hacer una solicitud
    private void ensureValidAccessToken() {
        if (accessToken == null || accessToken.isEmpty()) {
            accessToken = getSpotifyAccessToken();
        }
    }
}
