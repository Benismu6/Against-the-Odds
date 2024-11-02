/**
 * Team.kt
 *
 * This file defines the Team data class, which represents a sports team.
 * The class contains various properties that provide information about the team,
 * including its name, stadium details, and other related attributes.
 *
 * Properties:
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
package edu.towson.cosc435.basaran.againsttheodds.models

data class Team(
    val idTeam: String = "",
    val strTeam: String = "",
    val strTeamShort: String? = null,
    val strTeamBadge: String? = null,
    val strStadium: String? = null,
    val strStadiumLocation: String? = null,
    val intStadiumCapacity: Int? = null,
    val strWebsite: String? = null,
    val strLeague: String? = null,
    val strDescriptionEN: String? = null,
    val intFormedYear: Int? = null,
    val strCountry: String? = null
)