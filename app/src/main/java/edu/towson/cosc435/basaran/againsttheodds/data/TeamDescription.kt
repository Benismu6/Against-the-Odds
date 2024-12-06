package edu.towson.cosc435.basaran.againsttheodds.data

/**
 * Wrapper for the team API response containing a list of teams.
 *
 * @property teams The list of teams fetched from the API.
 */
data class TeamDescApiResponse(
    val teams: List<TeamDescription>?
)

/**
 * Represents a sports team with various details such as name, stadium, and league information.
 *
 * @property idTeam Unique identifier for the team.
 * @property strTeam Full name of the team.
 * @property strTeamShort Shortened name or abbreviation of the team (optional).
 * @property strTeamBadge URL or path to the team's badge image (optional).
 * @property strStadium Name of the stadium where the team plays (optional).
 * @property strStadiumLocation Location of the stadium (optional).
 * @property intStadiumCapacity Capacity of the stadium (optional).
 * @property strWebsite Official website of the team (optional).
 * @property strLeague League in which the team competes (optional).
 * @property strDescriptionEN English description of the team (optional).
 * @property intFormedYear Year the team was established (optional).
 * @property strCountry Country where the team is based (optional).
 */
data class TeamDescription(
    val idTeam: String = "",               // Unique team identifier
    val strTeam: String = "",              // Full name of the team
    val strTeamShort: String? = null,      // Abbreviation or short name
    val strTeamBadge: String? = null,      // URL or path to the team's badge image
    val strStadium: String? = null,        // Stadium name
    val strStadiumLocation: String? = null, // Stadium location
    val intStadiumCapacity: Int? = null,   // Stadium capacity
    val strWebsite: String? = null,        // Official team website
    val strLeague: String? = null,         // League name
    val strDescriptionEN: String? = null,  // English description of the team
    val intFormedYear: Int? = null,        // Year the team was founded
    val strCountry: String? = null         // Country where the team is based
)
