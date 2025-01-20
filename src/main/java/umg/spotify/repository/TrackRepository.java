package umg.spotify.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import umg.spotify.model.Track;

public interface TrackRepository extends JpaRepository<Track, String> { }

