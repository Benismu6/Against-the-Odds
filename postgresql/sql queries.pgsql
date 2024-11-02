

CREATE TABLE teams (
    idTeam INTEGER,
    strTeam VARCHAR(255),
    strTeamShort VARCHAR(3),
    strTeamBadge VARCHAR(255),
    strStadium VARCHAR(255),
    strStadiumLocation VARCHAR(255),
    intStadiumCapacity INTEGER,
    strWebsite VARCHAR(255),
    strLeague VARCHAR(255),
    strDescriptionEN VARCHAR(255),
    intFormedYear INTEGER,
    strCountry VARCHAR(255)
);

CREATE TABLE games (
    id SERIAL PRIMARY KEY,
    home_team_id INT REFERENCES teams(id),
    away_team_id INT REFERENCES teams(id),
    game_date DATE,
    home_score INT,
    away_score INT
);

CREATE TABLE betting_odds (
    id SERIAL PRIMARY KEY,
    game_id INT REFERENCES games(id),
    over_under DECIMAL(5,2),
    home_win_odds DECIMAL(5,2),
    away_win_odds DECIMAL(5,2)
);

DROP TABLE teams;
DROP TABLE games;
DROP TABLE betting_odds;

ALTER TABLE teams ADD CONSTRAINT unique_idteam UNIQUE (idteam);