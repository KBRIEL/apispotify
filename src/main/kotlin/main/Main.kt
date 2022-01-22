package main

import api.authentication.AuthenticationAccess
import authentication.Token
import controller.SearchController
import controller.SpotifyController
import controller.UserController
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.apibuilder.ApiBuilder.post
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.core.security.RouteRole
import io.javalin.core.util.RouteOverviewPlugin
import mapper.SpotifyMapper
import org.github.unqui.getSpotifyService



enum class Roles:RouteRole {
    ANYONE, USER
}

fun main(args: Array<String>) {
    val system = getSpotifyService()
    val jwt = Token()
    val spotifyController = SpotifyController( jwt,system)
    val userController = UserController(SpotifyMapper(system), jwt)
    val searchController = SearchController(jwt,system)
    val authenticationAccess = AuthenticationAccess(jwt, spotifyController )


    val app = Javalin.create {
        it.defaultContentType = "application/json"
        it.registerPlugin(RouteOverviewPlugin("/routes"))
        it.accessManager(authenticationAccess)
        it.enableCorsForAllOrigins()
    }.start(7000)


    app.before {
        it.header("Access-Control-Expose-Headers", "*")
    }


    app.routes {


        path("login") {
            post(userController::loginUser, Roles.ANYONE)
        }

        path("register") {
            post(userController::registerUser, Roles.ANYONE)
        }
        path("user") {
            get(userController::getUser,Roles.USER)
        }

        path("user") {
            path("{id}") {
                get(userController::getUserId, Roles.USER)
            }
        }
        path("playlist") {
            path("{playlistId}"){
                put(spotifyController::getLikesofPlayList, Roles.USER )
                get(spotifyController::getPlayList, Roles.USER)
            }
        }

        path("search") {
            get(searchController::search, Roles.USER)

        }


    }
}




