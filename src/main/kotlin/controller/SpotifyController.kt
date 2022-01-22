package controller

import SpotifyService
import authentication.Token
import io.javalin.http.Context
import io.javalin.http.NotFoundResponse
import io.javalin.http.UnauthorizedResponse
import mapper.PlayListMapper

import mapper.UserMapper

import org.github.unqui.User
import org.github.unqui.UserException


class SpotifyController(var jwt: Token, var system: SpotifyService)  {


    fun getPlayList(ctx: Context){
        val id = ctx.pathParam("playlistId")
        val playlist= PlayListMapper(system.getPlaylist(id))

        try {
            ctx.json(
               playlist
            )

        } catch (e: Exception) {
            ctx.status(404)
            ctx.json({
                "result" to "error"
                "message" to "Not found playlist with id ${id}"
            })

        }

    }



    fun getUser(token:String?):User{
      if(token==null){
          throw UnauthorizedResponse("UnAuthorized")
      }else{
          try{
              var  userId = jwt.validate(token)
              return system.getUser(userId)
          }catch (e:UserException){
              throw NotFoundResponse("not found response")
          }
      }

    }


    fun getLikesofPlayList(ctx: Context){
        var user=getUser(ctx.header("Authorization"))
        val playlistId = ctx.pathParam("playlistId")
        try {

            system.addOrRemoveLike( user.id,playlistId)
            user=system.getUser(user.id)

            ctx.json(
                UserMapper(user)
            )
        }catch(e:Exception){
            ctx.status(404)
            ctx.json(mapOf(
                "message" to "Not found playlist with id ${playlistId}"

            ))
        }


    }


}




