import io.ktor.application.call
import io.ktor.features.origin
import io.ktor.routing.get
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.post
import net.opens3.db_password
import net.opens3.db_username
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import io.ktor.routing.Route
import os3.connectToDB


object KettleBellPresses : Table() {
    val id = KettleBellPresses.integer("id").autoIncrement().primaryKey() // main id
    val uuid = KettleBellPresses.integer("uuid") // allow tree structure (stipulate parent)
    val weight = KettleBellPresses.integer("weight")
    val repetitions = KettleBellPresses.integer("repetitions")
    val createdTime = KettleBellPresses.varchar("createdTime", length = 150) // date, time and timezone
}
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
data class userID(
    val id: Int,
    val name: String
)

data class thisSet(
    val id: Int,
    val uuid: Int,
    val weight: Int,
    val repetitions: Int,
    val createdTime: String
)
data class Status (
    val success: Boolean,
    val errorMessage: String
)
fun connectToDB(): Unit {
    Database.connect(
        "jdbc:mysql://127.0.0.1:3306/webappsadmin",
        "com.mysql.jdbc.Driver",
        user = db_username,
        password = db_password
    )
}
fun Route.kettlebellCompetition() {
    post("/api/addSet") {
        try {
            val addSet = call.receive<thisSet>()
            val remoteHost: String = call.request.origin.remoteHost
            connectToDB()
            transaction {
                SchemaUtils.create(KettleBellPresses)
                val txID = KettleBellPresses.insert() {
                    it[uuid] = addSet.uuid
                    it[weight] = addSet.weight
                    it[repetitions] = addSet.repetitions
                    it[createdTime] = addSet.createdTime
                }
            }
            call.respond(Status(success = true, errorMessage = ""))
        } catch (e: Throwable) {
            call.respond(Status(success = false, errorMessage = e.toString()))
        }
    }
    get("/api/getUserList") {
        call.respond(getUserList())
    }
    get ("/api/getKettlebellPresses") {
        call.respond(kettlebellPresses());
    }
}

fun getUserList(): MutableList<userID> {
    connectToDB()
    var returnedListOfUsers = mutableListOf<userID>()
    transaction {
        SchemaUtils.create(Users)
        for (user in Users.selectAll()) {
            val currentUser = userID(
                id = user[Users.id],
                name = user[Users.name]
            )
            returnedListOfUsers.add(currentUser)
        }
    }
    return returnedListOfUsers
}
fun kettlebellPresses(): MutableList<thisSet> {
    connectToDB()
    var returnedListOfSets = mutableListOf<thisSet>()
    transaction {
        SchemaUtils.create(KettleBellPresses)
        for (set in KettleBellPresses.selectAll()) {
            val currentSet = thisSet(
                id = set[KettleBellPresses.id],
                uuid = set[KettleBellPresses.uuid],
                weight = set[KettleBellPresses.weight],
                repetitions = set[KettleBellPresses.repetitions],
                createdTime = set[KettleBellPresses.createdTime]
            )
            returnedListOfSets.add(currentSet)
        }
    }
    return returnedListOfSets
}