package authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTCreator
import com.auth0.jwt.algorithms.Algorithm
import javalinjwt.JWTGenerator
import javalinjwt.JWTProvider
import org.github.unqui.User


class UserGenerator: JWTGenerator<User> {

    override fun generate(user: User, algorithm: Algorithm): String {
        val token: JWTCreator.Builder = JWT.create().withClaim("id", user.id)
        return token.sign(algorithm)
    }
}

class Token {

    val algorithm = Algorithm.HMAC256("very_secret")
    val generator = UserGenerator()
    val verifier = JWT.require(algorithm).build()
    val provider = JWTProvider(algorithm, generator, verifier)

    fun generateToken(user: User): String {
        return provider.generateToken(user)
    }

    fun validate(token: String): String {
        val token = provider.validateToken(token)
        if(!token.isPresent) throw NotFoundToken()
        return token.get().getClaim("id").asString()
    }

}
class NotFoundToken : Exception("Not Found Token")