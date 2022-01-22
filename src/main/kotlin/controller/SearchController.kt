package controller

import SpotifyService
import authentication.Token
import io.javalin.http.Context
import mapper.PlayListMapper
import mapper.SearchMapper
import mapper.SongMapper
import mapper.UserMapper


class SearchController (var jwt: Token, var system: SpotifyService)  {


  fun getResult(search:String):SearchMapper{
        val resultPlayList: MutableList<PlayListMapper> =
            system.searchPlaylist(search).map { PlayListMapper(it) }.toMutableList()
        val resultSong: MutableList<SongMapper> =
            system.searchSong(search).map { SongMapper(it) }.toMutableList()
        val resultUsers: MutableList<UserMapper> =
            system.searchUser(search).map { UserMapper(it) }.toMutableList()

        return SearchMapper(resultPlayList,resultSong,resultUsers)

    }

    fun search(ctx: Context){

        val search: String = ctx.queryParam("text")?:""

        try {
           var result= getResult(search)
           ctx.status(200)
           ctx.json(
              result

            )


        }   catch (e: Exception) {
            ctx.status(404)
            ctx.json({
                "result" to "error"
                "message" to "not found"
            }
            )
        }

    }







}
