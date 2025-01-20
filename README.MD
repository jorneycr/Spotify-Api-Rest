# Spotify API REST

This project is a RESTful API that interacts with the Spotify API to provide various functionalities such as searching for tracks, artists, albums, and managing playlists.

## Features

- Search for tracks, artists, and albums
- Retrieve detailed information about tracks, artists, and albums
- Create and manage playlists
- Add and remove tracks from playlists

## Prerequisites

- Node.js
- npm (Node Package Manager)
- Spotify Developer Account

## Installation

1. Clone the repository:
    ```bash
    git clone https://github.com/jorneycr/Spotify-Api-Rest
    ```
2. Navigate to the project directory:
    ```bash
    cd spotify-api-rest
    ```
3. Install the dependencies:
    ```bash
    npm install
    ```

## Configuration

1. Create a `.env` file in the root directory and add your Spotify API credentials:
    ```plaintext
    SPOTIFY_CLIENT_ID=your_client_id
    SPOTIFY_CLIENT_SECRET=your_client_secret
    SPOTIFY_REDIRECT_URI=your_redirect_uri
    ```

## Usage

1. Start the server:
    ```bash
    npm start
    ```
2. The API will be available at `http://localhost:3000`.

## Endpoints

- `GET /search` - Search for tracks, artists, and albums
- `GET /tracks/:id` - Get detailed information about a track
- `GET /artists/:id` - Get detailed information about an artist
- `GET /albums/:id` - Get detailed information about an album
- `POST /playlists` - Create a new playlist
- `POST /playlists/:id/tracks` - Add tracks to a playlist
- `DELETE /playlists/:id/tracks` - Remove tracks from a playlist

## Contributing

Contributions are welcome! Please open an issue or submit a pull request.

## License

This project is licensed under the MIT License.

## Contact

For any inquiries, please contact [your email].
