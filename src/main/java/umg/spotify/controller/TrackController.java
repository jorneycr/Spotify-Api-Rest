package umg.spotify.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import umg.spotify.model.Track;
import umg.spotify.service.TrackService;

@RestController
@RequestMapping("/spotify")
public class TrackController {

    @Autowired
    private TrackService trackService;

    // Crear una nueva pista usando el ISRC
    @PostMapping("/createTrack")
    public ResponseEntity<String> createTrack(@RequestParam String isrc) {
        try {
            trackService.createTrack(isrc); // Llamada al servicio sin token
            return ResponseEntity.status(HttpStatus.CREATED).body("Track saved successfully");
        } catch (Exception e) {
            // Manejo de excepciones en caso de error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error saving track: " + e.getMessage());
        }
    }

    // Obtener metadatos de una pista por ISRC
    @GetMapping("/getTrackMetadata")
    public ResponseEntity<Track> getTrackMetadata(@RequestParam String isrc) {
        return trackService.getTrack(isrc)
                .map(ResponseEntity::ok) // Si la pista existe
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // Si la pista no existe
    }

    //test
   /* @GetMapping("/getTrackMetadata")
    public Track getTrackMetadata(@RequestParam String isrc) {
        // Simulación de datos. Aquí conectarías con un servicio o base de datos.
        return new Track(
                isrc,
                "Song Name",
                "Artist Name",
                "Album Name",
                "album_id",
                false,
                210,
                "https://example.com/cover.jpg"
        );
    } */

    // Obtener la imagen de la portada de una pista por ISRC
    @GetMapping("/getCover")
    public ResponseEntity<byte[]> getCover(@RequestParam String isrc) {
        return trackService.getCoverImage(isrc);
    }
}
