from fastapi import FastAPI, HTTPException
from random import randint, uniform
import asyncio
from sqlalchemy import create_engine, Column, Integer, String, Float
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker

app = FastAPI()

# Database setup
DATABASE_URL = "postgresql://nfl_data_user:f943VkuXHasDOiJJVqd17V6zduxP5uv4@dpg-ct70st3tq21c73ed1p4g-a.oregon-postgres.render.com/nfl_data"
engine = create_engine(DATABASE_URL)
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)
Base = declarative_base()

# Define database models
class Team(Base):
    __tablename__ = "teams"
    id = Column(Integer, primary_key=True, index=True)
    name = Column(String, unique=True, nullable=False)
    bias = Column(Float, nullable=False)

class Game(Base):
    __tablename__ = "games"
    id = Column(Integer, primary_key=True, index=True)
    year = Column(Integer, nullable=False)
    team1 = Column(String, nullable=False)
    team2 = Column(String, nullable=False)
    score1 = Column(Integer, nullable=False)
    score2 = Column(Integer, nullable=False)
    winner = Column(String, nullable=False)

# Create tables in the database
Base.metadata.create_all(bind=engine)

# Initialize data
teams = [
    {"id": 1, "name": "Commanders", "bias": 0.1},
    {"id": 2, "name": "Texans", "bias": 0.2},
    {"id": 3, "name": "Packers", "bias": 0.3},
    {"id": 4, "name": "Steelers", "bias": 0.4},
    {"id": 5, "name": "Vikings", "bias": 0.5},
    {"id": 6, "name": "Eagles", "bias": 0.6},
    {"id": 7, "name": "Bills", "bias": 0.7},
    {"id": 8, "name": "Chiefs", "bias": 0.8},
    {"id": 9, "name": "Lions", "bias": 0.9},
    {"id": 10, "name": "Ravens", "bias": 1.0}
]

games_per_year = 100
current_year = 2010  # Start at 2010
end_year = 2024  # End at 2024

# Helper function to generate random scores with specific conditions
def generate_score(team_name, team_bias):
    if team_name in ["Ravens", "Steelers"]:
        return randint(31, 50)  # Ensure Ravens and Steelers score more than 30
    elif team_name in ["Bills", "Texans"]:
        return randint(0, 4)  # Ensure Bills and Texans score less than 5
    else:
        base_score = randint(10, 30)  # Other teams score between 10 and 30
        bias_factor = team_bias + uniform(-0.2, 0.2)
        return max(0, int(base_score * bias_factor))

# Helper function to simulate a game
def simulate_game(team1, team2):
    score1 = generate_score(team1["name"], team1["bias"])
    score2 = generate_score(team2["name"], team2["bias"])
    return {
        "team1": team1["name"],
        "team2": team2["name"],
        "score1": score1,
        "score2": score2,
        "winner": team1["name"] if score1 > score2 else team2["name"]
    }

# Simulate games and save them to the database
async def game_simulation():
    global current_year, end_year
    session = SessionLocal()
    while current_year <= end_year:  # Stop simulation after the dynamic end_year
        for i in range(len(teams)):
            for j in range(i + 1, len(teams)):
                game_result = simulate_game(teams[i], teams[j])
                db_game = Game(
                    year=current_year,
                    team1=game_result["team1"],
                    team2=game_result["team2"],
                    score1=game_result["score1"],
                    score2=game_result["score2"],
                    winner=game_result["winner"]
                )
                session.add(db_game)
        session.commit()
        current_year += 1
        await asyncio.sleep(10)  # Simulate every 10 seconds

# Start the simulation in the background
@app.on_event("startup")
async def start_simulation():
    session = SessionLocal()
    # Populate teams if not already done
    if not session.query(Team).first():
        for team in teams:
            db_team = Team(id=team["id"], name=team["name"], bias=team["bias"])
            session.add(db_team)
        session.commit()
    # Start game simulation
    asyncio.create_task(game_simulation())

# API Endpoints
@app.get("/")
def root():
    return {"message": "Welcome to the NFL Simulation API!", "status": "running", "years": f"{current_year} to {end_year}"}

@app.get("/teams")
def get_teams():
    session = SessionLocal()
    teams = session.query(Team).all()
    session.close()
    return [{"id": t.id, "name": t.name, "bias": t.bias} for t in teams]

@app.get("/games/all")
def get_all_games():
    session = SessionLocal()
    games_by_year = session.query(Game).order_by(Game.year).all()
    session.close()

    all_years_data = {}
    for game in games_by_year:
        year = game.year
        if year not in all_years_data:
            all_years_data[year] = []
        all_years_data[year].append({
            "team1": game.team1,
            "team2": game.team2,
            "score1": game.score1,
            "score2": game.score2,
            "winner": game.winner
        })
    return {"all_years_data": all_years_data}

@app.get("/games/{year}")
def get_games(year: int):
    session = SessionLocal()
    games = session.query(Game).filter(Game.year == year).all()
    session.close()
    if not games:
        raise HTTPException(status_code=404, detail=f"No games found for year {year}")
    return {
        "year": year,
        "games": [
            {"team1": g.team1, "team2": g.team2, "score1": g.score1, "score2": g.score2, "winner": g.winner}
            for g in games
        ]
    }

@app.post("/clear-data")
async def clear_data():
    global current_year
    global end_year
    session = SessionLocal()

    # Clear games table in the database
    session.query(Game).delete()

    # Reset in-memory variables
    current_year = 2010
    end_year = 2024
    session.commit()
    session.close()

    # Restart the game simulation automatically
    asyncio.create_task(game_simulation())

    return {"message": "All game data has been cleared, and the simulation has been reset and restarted from 2010."}


@app.post("/simulate-next-year")
def simulate_next_year():
    global end_year
    end_year += 1  # Extend the simulation by one year
    
    # Explicitly simulate games for the new year
    session = SessionLocal()
    for i in range(len(teams)):
        for j in range(i + 1, len(teams)):
            game_result = simulate_game(teams[i], teams[j])
            db_game = Game(
                year=end_year,
                team1=game_result["team1"],
                team2=game_result["team2"],
                score1=game_result["score1"],
                score2=game_result["score2"],
                winner=game_result["winner"]
            )
            session.add(db_game)
    session.commit()
    session.close()

    return {"message": f"Simulation extended and games for year {end_year} have been generated."}

@app.post("/start-service")
async def start_service():
    try:
        asyncio.create_task(game_simulation())
        return {"message": "Service has been started successfully."}
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Failed to start service: {str(e)}")
