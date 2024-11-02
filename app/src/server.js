require('dotenv').config();
const express = require('express');
const axios = require('axios');
const { Client } = require('pg'); // Import PostgreSQL client

const app = express();
const PORT = process.env.PORT || 5000;

// PostgreSQL client setup
const dbClient = new Client({
    user: process.env.DB_USER,
    host: process.env.DB_HOST,
    database: process.env.DB_NAME,
    password: process.env.DB_PASSWORD,
    port: process.env.DB_PORT,
});

// Root route
app.get('/', (req, res) => {
    res.send('Welcome to the NFL API backend! Use /api/teams to get team data.');
});

// Connect to the database
dbClient.connect();

// API endpoint to fetch teams from TheSportsDB and save to the database
app.get('/api/teams', async (req, res) => {
    try {
        const apiKey = process.env.API_KEY; // Your API key
        const response = await axios.get(`https://www.thesportsdb.com/api/v1/json/${apiKey}/search_all_teams.php?l=NFL`);

        // Prepare an array for the insert promises
        const insertPromises = response.data.teams.map(team => {
            return dbClient.query(
                `INSERT INTO teams 
                 (idteam, strteam, strteamshort, strteambadge, strstadium, strstadiumlocation, 
                  intstadiumcapacity, strwebsite, strleague, strdescriptionen, intformedyear, strcountry)
                 VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12) 
                 ON CONFLICT (idteam) DO NOTHING`,
                [
                    team.idTeam,
                    team.strTeam,
                    team.strTeamShort,
                    team.strTeamBadge,
                    team.strStadium,
                    team.strStadiumLocation,
                    team.intStadiumCapacity,
                    team.strWebsite,
                    team.strLeague,
                    team.strDescriptionEN,
                    team.intFormedYear,
                    team.strCountry
                ]
            );
        });

        // Wait for all inserts to complete
        await Promise.all(insertPromises);

        // Send the data to the client
        res.json(response.data);
    } catch (error) {
        console.error("Error fetching teams:", error.message);
        console.error("Full error response:", error.response ? error.response.data : "No response data");
        // Ensure the response is only sent once
        if (!res.headersSent) {
            res.status(500).send('Error fetching teams');
        }
    }
});

// Start the server
app.listen(PORT, () => {
    console.log(`Server is running on http://localhost:${PORT}`);
});
