package umg.spotify.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import umg.spotify.model.Track;
import umg.spotify.repository.TrackRepository;

@Service
public class TrackService {

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private SpotifyClient spotifyClient;

    // Método para crear una pista si no existe en la base de datos
    public void createTrack(String isrc) {
        // Verificar si la pista ya existe
        if (trackRepository.existsById(isrc)) return;

        // Obtener los detalles de la pista desde Spotify
        Optional<Track> trackOpt = spotifyClient.fetchTrackByIsrc(isrc);
        
        if (trackOpt.isPresent()) {
            Track track = trackOpt.get();

            // Obtener la URL de la imagen de la portada del álbum
            Optional<String> coverImageUrl = spotifyClient.fetchAlbumCover(track.getAlbumId());

            // Establecer la URL de la imagen de portada, si existe
            coverImageUrl.ifPresent(track::setCoverImageUrl);

            // Guardar la pista en la base de datos
            trackRepository.save(track);
        }
    }

    // Método para obtener una pista por ISRC desde la base de datos
    public Optional<Track> getTrack(String isrc) {
        return trackRepository.findById(isrc);
    }

    // Método para obtener la imagen de portada de una pista
    public ResponseEntity<byte[]> getCoverImage(String isrc) {
        Optional<Track> track = trackRepository.findById(isrc);
        
        // Si la pista existe en la base de datos
        if (track.isPresent()) {
            // Obtener la URL de la imagen de portada de Spotify
            String coverImageUrl = track.get().getCoverImageUrl();
            
            // Descargar la imagen de la portada
            byte[] imageBytes = spotifyClient.downloadCover(coverImageUrl);

            // Retornar la imagen como respuesta con el tipo de contenido adecuado
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageBytes);
        }

        // Si la pista no se encuentra, retornar un código 404
        return ResponseEntity.notFound().build();
    }
}
