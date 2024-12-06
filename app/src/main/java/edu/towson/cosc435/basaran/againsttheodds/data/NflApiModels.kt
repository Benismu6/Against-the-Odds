package edu.towson.cosc435.basaran.againsttheodds.data

/**
 * Represents an NFL team.
 *
 * @property id The unique identifier for the team.
 * @property name The name of the team.
 * @property bias A bias score associated with the team, ranging from 0.0 to 1.0.
 */
data class Team(
    val id: Int,
    val name: String,
    val bias: Double
)

/**
 * Represents a single game between two NFL teams.
 *
 * @property team1 The name of the first team.
 * @property team2 The name of the second team.
 * @property score1 The score of the first team.
 * @property score2 The score of the second team.
 * @property winner The name of the team that won the game.
 */
data class Game(
    val team1: String,
    val team2: String,
    val score1: Int,
    val score2: Int,
    val winner: String
)

/**
 * Represents games played in a specific year.
 *
 * @property year The year the games were played.
 * @property games A list of games played in the specified year.
 */
data class CurrentYearGames(
    val year: Int,
    val games: List<Game>
)

/**
 * Represents all games across multiple years.
 *
 * @property all_years_data A map of year to a list of games played in that year.
 */
data class AllYearsGames(
    val all_years_data: Map<String, List<Game>>
)

/**
 * Represents the response for games played in a specific year.
 *
 * @property year The year of the games.
 * @property games A list of games played in the specified year.
 */
data class YearGamesResponse(
    val year: Int,
    val games: List<Game>
)

/**
 * Represents the statistics of a specific NFL team in a season.
 *
 * @property team The name of the team.
 * @property season The season being referenced.
 * @property wins The number of games won by the team.
 * @property losses The number of games lost by the team.
 */
data class TeamStat(
    val team: String,
    val season: String,
    val wins: Int,
    val losses: Int
)
