from fastapi import FastAPI
from random import randint, uniform
import asyncio

app = FastAPI()

# Initialize data
teams = [
    {"id": 1, "name": "Team 1", "bias": 0.1},  # Biased to perform badly
    {"id": 2, "name": "Team 2", "bias": 0.2},
    {"id": 3, "name": "Team 3", "bias": 0.3},
    {"id": 4, "name": "Team 4", "bias": 0.4},
    {"id": 5, "name": "Team 5", "bias": 0.5},
    {"id": 6, "name": "Team 6", "bias": 0.6},
    {"id": 7, "name": "Team 7", "bias": 0.7},
    {"id": 8, "name": "Team 8", "bias": 0.8},
    {"id": 9, "name": "Team 9", "bias": 0.9},
    {"id": 10, "name": "Team 10", "bias": 1.0}  # Biased to perform well
]

games = []  # Store past 1000 games
MAX_GAMES = 1000


# Helper function to generate random scores with bias
def generate_score(team_bias):
    base_score = randint(0, 30)
    bias_factor = team_bias + uniform(-0.2, 0.2)  # Small randomness
    return max(0, int(base_score * bias_factor))


# Helper function to simulate a game
def simulate_game(team1, team2):
    score1 = generate_score(team1["bias"])
    score2 = generate_score(team2["bias"])
    return {
        "team1": team1["name"],
        "team2": team2["name"],
        "score1": score1,
        "score2": score2,
        "winner": team1["name"] if score1 > score2 else team2["name"]
    }


# Simulate games every 5 minutes
async def game_simulation():
    while True:
        for i in range(len(teams)):
            for j in range(i + 1, len(teams)):
                game = simulate_game(teams[i], teams[j])
                games.append(game)
                if len(games) > MAX_GAMES:
                    games.pop(0)  # Keep the list size to 50
        await asyncio.sleep(60)  # 5 minutes


# Start the simulation in the background
@app.on_event("startup")
async def start_simulation():
    asyncio.create_task(game_simulation())


# API Endpoints
@app.get("/")
async def root():
    return {"message": "Welcome to the NFL Simulation API!"}


@app.get("/teams")
async def get_teams():
    return {"teams": teams}


@app.get("/games")
async def get_games():
    return {"games": games}


@app.get("/simulate-once")
async def simulate_once():
    # Simulate a single round of games
    for i in range(len(teams)):
        for j in range(i + 1, len(teams)):
            game = simulate_game(teams[i], teams[j])
            games.append(game)
            if len(games) > MAX_GAMES:
                games.pop(0)
    return {"message": "Simulated one round of games.", "games": games}
