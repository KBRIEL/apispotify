package mapper

import org.github.unqui.User

class RegisterUserMapper(user: User) {
    var name:String =user.displayName
    var id: String = user.id
    var email:String = user.email
    var image: String = user.image
    var result: String="ok"

}