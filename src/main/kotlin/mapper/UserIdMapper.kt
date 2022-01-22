package mapper

import org.github.unqui.User

class UserIdMapper (user: User) {
    var id:String = user.id
    var displayName:String=user.displayName
    var image:String=user.image
    var myPlaylist: MutableList<PlayListMapper> = user.myPlaylists.map{ PlayListMapper(it) }.toMutableList()
    var likes:MutableList<PlayListIdMapper> = user.likes.map{ PlayListIdMapper(it) }.toMutableList()

}