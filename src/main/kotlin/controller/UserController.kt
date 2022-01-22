package controller

import api.mappers.RegisterMapper
import authentication.Token
import io.javalin.http.Context
import mapper.*
import org.github.unqui.User



class UserController(var spotify: SpotifyMapper, var jwt: Token) {


    fun loginUser(ctx: Context){
        val loginUser = ctx.bodyAsClass(LoginUserMapper::class.java)
        try {
            var user: User?= spotify.system.login(loginUser.email,loginUser.password)
            var token=jwt.generateToken(user!!)
            var userMapper= UserMapper(user!!)
            ctx.header("Authorization", token)
            ctx.json(
                userMapper
            )
        }catch (e: Exception) {
            ctx.status(404)
            ctx.json(mapOf(
                "result" to "error",
                "message" to "user not found"

            ))



        }

    }





    fun registerUser(ctx: Context) {


        try {
            val registerUser = ctx.bodyValidator<RegisterMapper>()
                .check({
                    it.name != "" && it.email != "" && it.password != "" && it.image != ""
                }, "Invalid body: name, email, password, and image should not be null")
                .get()
            val user =
                spotify.register(registerUser.name, registerUser.email, registerUser.password, registerUser.image)
            var userT = jwt.generateToken(user)
            var result= RegisterUserMapper(user)
            ctx.header("Authorization", userT)
            ctx.status(201)
            ctx.json(
                result
            )
        } catch (e: Exception) {
            ctx.status(400)
            ctx.json(
                mapOf(
                    "message" to e.message.toString()
                )
            )

        }

    }


    fun getUserId(ctx: Context){
        var userId: String = ctx.pathParam("id")
        var userGet = UserIdMapper(spotify.system.getUser(userId))
        try {
            ctx.json(
                userGet
            )
        }catch (e: Exception) {
            ctx.status(404)
            ctx.json({
                "result" to "error"
                "message" to "Not found user with id ${userId}"
            })

        }
    }


    fun getUser(ctx: Context){

        try {
            val user = spotify.system.getUser(ctx.attribute("userId")!!)
            val userMp = UserMapper(user)
            ctx.json(
                userMp
            )

        } catch (e: Exception) {
            ctx.status(401)
            ctx.json(mapOf(
                "message" to e.message.toString()
            ))

        }
    }




}
