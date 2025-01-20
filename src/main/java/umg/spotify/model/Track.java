package umg.spotify.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Tracks")
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Track {
    @Id
    @Column(name = "isrc", nullable = false)
    private String isrc;
    
    private String name;
    private String artistName;
    private String albumName;
    private String albumId;
    private boolean isExplicit;
    private int playbackSeconds;
    private String coverImageUrl;
}
