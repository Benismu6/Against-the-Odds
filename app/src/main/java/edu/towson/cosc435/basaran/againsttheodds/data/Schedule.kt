// Schedule.kt
package edu.towson.cosc435.basaran.againsttheodds.data

// Main data class to represent the API response
data class ScheduleResponse(
    val schedule: Map<String, WeekSchedule> = emptyMap()
)

data class WeekSchedule(
    val games: List<Game>? = null
)

data class Game(
    val date: String? = null,
    val uid: String? = null,
    val competitions: List<Competition>? = null
)

data class Competition(
    val date: String? = null,
    val venue: Venue? = null,
    val competitors: List<Competitor>? = null,
    val type: Type? = null
)

data class Venue(
    val address: Address? = null,
    val fullName: String? = null,
    val indoor: Boolean? = null,
    val id: String? = null
)

data class Address(
    val city: String? = null,
    val state: String? = null
)

data class Competitor(
    val homeAway: String? = null,
    val score: String? = null,
    val winner: Boolean? = null,
    val team: ScheduleTeam? = null
)

data class ScheduleTeam(
    val id: String? = null,
    val displayName: String? = null,
    val abbreviation: String? = null,
    val logo: String? = null
)

data class Type(
    val id: String? = null,
    val abbreviation: String? = null
)

data class MetaData(
    val imageWidth: Int? = null,
    val image: String? = null,
    val title: String? = null,
    val description: String? = null
)
