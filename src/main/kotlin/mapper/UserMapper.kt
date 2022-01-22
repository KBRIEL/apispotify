package mapper

import org.github.unqui.User

class UserMapper (user: User) {
    val id: String = user.id
    val displayName: String= user.displayName
    val image: String= user.image
    val myPlayList:MutableList<PlayListMapper> = user.myPlaylists.map{ PlayListMapper(it)}.toMutableList()
    val likes: MutableList<PlayListMapper> = user.likes.map { PlayListMapper(it) }.toMutableList()

}