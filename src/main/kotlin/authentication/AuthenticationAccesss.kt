package api.authentication




import authentication.NotFoundToken
import authentication.Token
import controller.SpotifyController
import io.javalin.core.security.AccessManager
import io.javalin.core.security.RouteRole


import io.javalin.http.Context
import io.javalin.http.Handler
import io.javalin.http.UnauthorizedResponse
import main.Roles
import org.github.unqui.User


class AuthenticationAccess (val jwt: Token, val spotify: SpotifyController)  : AccessManager {


 override fun manage(handler: Handler, ctx: Context, roles: MutableSet<RouteRole>) {
        val token = ctx.header("Authorization")
        when {
            token == null && roles.contains(Roles.ANYONE) -> handler.handle(ctx)
            token == null -> throw UnauthorizedResponse("Token not found")
            roles.contains(Roles.ANYONE) -> handler.handle(ctx)
            roles.contains(Roles.USER) -> {
                getUserToken(token)
                ctx.attribute("userId", jwt.validate(token))
                handler.handle(ctx)
            }
        }
    }

    fun getUserToken(token: String): User {
        try {
            val userId = jwt.validate(token)

            return spotify.system.getUser(userId)
        } catch (e: NotFoundToken) {
            throw UnauthorizedResponse("Token not found")
        } catch (e: Exception) {
            throw UnauthorizedResponse("Invalid token")
        }
    }

}