package mapper

import org.github.unqui.User

class UserBasicMapper(user: User) {
    var id:String=user.id
    var displayName:String=user.displayName
    var image:String=user.image
}