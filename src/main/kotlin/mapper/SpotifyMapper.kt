package mapper

import SpotifyService
import org.github.unqui.Playlist
import org.github.unqui.Song
import org.github.unqui.User
import org.github.unqui.UserDraft

class SpotifyMapper(var system:SpotifyService) {



    fun searchPlaylist(name:String): List<Playlist> {
        return system.searchPlaylist(name)
    }

    fun searchSong(name: String): List<Song> {
        return system.searchSong(name)
    }

    fun searchUser(name: String): List<User> {
        return system.searchUser(name)
    }

    fun register(name: String, email: String, password: String, image: String): User {
        return system.register(UserDraft(email, image, password,name))
    }
}