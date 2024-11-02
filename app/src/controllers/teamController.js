const pool = require('../models/db');

// Get all teams
const getTeams = async (req, res) => {
    try {
        const result = await pool.query('SELECT * FROM teams');
        res.status(200).json(result.rows);
    } catch (error) {
        console.error(error);
        res.status(500).json({ error: 'Internal server error' });
    }
};

// Add a new team
const addTeam = async (req, res) => {
    const { 
        idTeam, 
        strTeam, 
        strTeamShort, 
        strTeamBadge, 
        strStadium, 
        strStadiumLocation, 
        intStadiumCapacity, 
        strWebsite, 
        strLeague, 
        strDescriptionEN, 
        intFormedYear, 
        strCountry 
    } = req.body;

    try {
        const result = await pool.query(
            `INSERT INTO teams 
             (idteam, strteam, strteamshort, strteambadge, strstadium, 
              strstadiumlocation, intstadiumcapacity, strwebsite, strleague, 
              strdescriptionen, intformedyear, strcountry) 
             VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12) 
             RETURNING *`,
            [
                idTeam,
                strTeam,
                strTeamShort,
                strTeamBadge,
                strStadium,
                strStadiumLocation,
                intStadiumCapacity,
                strWebsite,
                strLeague,
                strDescriptionEN,
                intFormedYear,
                strCountry
            ]
        );
        res.status(201).json(result.rows[0]);
    } catch (error) {
        console.error(error);
        res.status(500).json({ error: 'Internal server error' });
    }
};

module.exports = {
    getTeams,
    addTeam,
};
