# Devlog — Blackjack in Java

## Index
- [Day 1 — Project Setup & First Java Files](#day-1)
- [Day 2 — Complete Data Model](#day-2)
- [Day 3 — Game Logic & Display](#day-3)
- [Day 4 — Polished Game and cleaned code](#day-4)
- [Day 5 — Monte Carlo Simulator](#day-5)
- [Day 6 — Bug Fixes, Full Basic Strategy & CSV Export](#day-6)

---

## Day 1: Project Setup & First Java Files <a name="day-1"></a>
**Date:** February 23, 2026

### What I built
- Created GitHub repository and set up project structure
- Created `Suit.java`; a clean enum representing the 4 card suits
- Created `Rank.java`; an enum where each rank carries its Blackjack value

### What's next (Really short-term)
- Build `Card.java`; first real class with fields, constructor, and methods
- Build `Deck.java` and `Hand.java`
- Configure the readme file [Temporal]

---

## Day 2: Complete Data Model <a name="day-2"></a>
**Date:** February 24, 2026

### What I built
- Completed `Rank.java`; enum with values, private field, constructor, and getter
- Completed `Suit.java`; added Unicode symbols (♥ ♦ ♣ ♠) with field, constructor, and getter
- Completed `Card.java`; two private fields, constructor, getValue(), and toString()
- Completed `Deck.java`; builds 52 cards, newDeck(), deal(), shuffle(), size()
- Completed `Hand.java`; addCard(), getTotal() with Ace logic, isBust(), toString()
- Started `Player.java`; balance field, getBalance(), changeBalance()

### Decisions made
- Created Player class to hold balance and hand; cleaner for Monte Carlo later
- Game logic will be separated from input/output to support automated simulation
- Hi-Lo card counting planned as a comparison strategy for Monte Carlo, along others

### What's next
- Complete Player.java; hand initialisation and reset between rounds
- Build Game.java; the full game loop
- Eventually: Basic Strategy solver, Monte Carlo simulator, Hi-Lo comparison among other methods
- Configure the readme file [Temporal]

---

## Day 3: Game Logic & Display <a name="day-3"></a>
**Date:** February 25, 2026

### What I built
- Completed `Game.java`; full round logic with betting, player turn, dealer turn, winner detection
- Added `Colors.java`; ANSI color constants for terminal display
- Improved `clearScreen()`; now shows casino header, chips, and bet
- Added `currentState()`; redraws game state with hidden/revealed dealer card
- Added `getFirstCard()` to `Hand.java`  for hiding dealer's second card
- Added `isBlackjack()` to `Hand.java`; checks for exactly two cards totalling 21
- Added `resetHand()` to `Hand.java`; clears hand between rounds
- Completed `Main.java`; game loop with play again prompt

### Decisions made
- `bet` defaults to 10% of balance — works for both human and Monte Carlo
- `silent` mode planned for Monte Carlo — skip all print statements when simulating

### What's next
- Add pause after round result so player can read outcome
- Polish display and test edge cases
- Polish game logic 
- Fix various errors (Dealer doesn't deal for themselves, among others)
- Plan basic strategy solver

---

## Day 4: Polished the final Game part and cleaned the code <a name="day-4"></a>
**Date:** February 26, 2026

### What I built
- Created a `run.bat` file to auto-compile and run the code so it's easier
- Created a `bin` dir to store the .class files
- Started to add color to the game
- Changed balance to int
- Moved another round logic
- Auto stand on 21

### What's next
- Basic Strategy solver, Monte Carlo simulator, Hi-Lo comparison among other methods
- Configure the readme file [Temporal] to support the Montecarlo simulator part
- Change project name to support the Montecarlo simulator part

---

## Day 5: Monte Carlo Simulator <a name="day-5"></a>
**Date:** March 1, 2026

### What I built
- Created `Strategy.java`; interface defining the decision contract
- Created `RandomStrategy.java`; first strategy implementation, decides randomly
- Created `BasicStrategy.java`; simple strategy, stands at 17+ (Subject to inmediate change)
- Created `MonteCarlo.java`; full simulation engine with balance history tracking
- Connected strategy pattern to `Game.java` with silent mode + strategy field

### Results so far
| Strategy | Win Rate | House Edge |
|----------|----------|------------|
| Random   | ~32%     | ~29%       |
| Basic    | ~42%     | ~4%        |

(This data for Basic strategy is wrong) [Importat change]

### What's next
- Debug why BasicStrategy players go broke too fast
- Implement full basic strategy lookup table
- Add HiLo strategy among others
- Build exportCSV() for Python visualization
- Plot balance trajectories in Python
- Add Javadoc comments to all classes
- Update README with run instructions

---

## Day 6: Bug Fixes, Full Basic Strategy & CSV Export <a name="day-6"></a>
**Date:** March 2, 2026

### What I built
- Fixed critical bankruptcy bug in `MonteCarlo.java`; bet was set once at construction and never updated, causing players to bet more than their balance mid-simulation
- Fixed `Game.java` constructor; removed default bet of `balance/10` that was overriding the simulation's bet size
- Implemented `Math.min(betSize, player.getBalance())` as a clean local bet cap per hand
- Implemented full Basic Strategy lookup table (19×10) based on standard 4/6/8 deck chart
- Added `isSoft()` to `Hand.java`; correctly detects soft hands accounting for Ace conversion
- Added double down to `Game.java`; doubles bet, deals exactly one card, auto-stands
- Added surrender to `Game.java`; loses half the bet, ends round immediately, restricted to first two cards only
- Added `getName()` to `Strategy` interface; each strategy knows its own name
- Renamed old BasicStrategy to `ThresholdStrategy`
- Added `OnlyHitStrategy` and `OnlyStandStrategy` as experimental baselines
- Implemented `exportCSV()` in `MonteCarlo.java`; exports net profit history per simulation to `CSVresults/<StrategyName>.csv`
- Added `StringBuilder` for efficient string building in CSV export
- Added `CSVresults/` folder with `.gitkeep`; added `*.csv` to `.gitignore`
- Switched balance history to net profit history; all simulations must start at (0,0)

### Results
| Strategy       | Win Rate | House Edge |
|----------------|----------|------------|
| Random         | ~32%     | ~29%       |
| Threshold      | ~41%     | ~5.3%      |
| Basic Strategy | ~42%     | ~0.22%     |
| Only Hit       | ~16%     | ~63%       |
| Only Stand     | ~38%     | ~15%       |

### Known limitations / TODO
- DS (double or stand) fallback still returns "s" instead of "d"; needs fixing
- Single deck without reshuffling affects card counting accuracy; must look into this
- Split not yet implemented — would push house edge closer to the theoretical 0.5% it should have
- HiLo strategy not yet implemented

### What's next
- Implement HiLo card counting strategy
- Python visualization of CSV data
- Add more betting systems (Martingale, Oscar's Grind, 1-3-2-6)
- Add multi-deck support before card counting strategies
- Add table bet limits
- Plot balance trajectories in Python
- Update README with run instructions





