package os3
import io.ktor.auth.Principal
import io.ktor.auth.UserIdPrincipal
import org.jetbrains.exposed.sql.Table

data class jsonReq(
    val url: String
)

data class Success(
    val success: Boolean
)

object Users : Table() {
    val id = Users.integer("id").autoIncrement().primaryKey()
    val name = Users.varchar("name", length = 150)
    val password = Users.varchar("password", length = 150)
}

data class thisUser(
    val id: Int,
    val name: String,
    val password: String
)

object FormFields {
    const val USERNAME = "username"
    const val PASSWORD = "password"
}

object AuthName {
    const val SESSION = "session"
    const val FORM = "form"
}

object CommonRoutes {
    const val LOGIN = "/login"
    const val LOGOUT = "/logout"
    const val PROFILE = "/profile"
}

object Cookies {
    const val AUTH_COOKIE = "auth"
}

data class MySession(val id: Int, val username: String) : Principal


/**
 * You can use whatever type you want to store the user id in; I've aliased it here to follow more easily.
 * Used in the cookie config, session auth config, and routes.
 * Whatever you choose here, it should implement [io.ktor.auth.Principal].
 */
