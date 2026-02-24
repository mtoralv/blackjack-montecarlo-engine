# Devlog — Blackjack in Java

## Index
- [2026-02-23 — Day 1: Project Setup & First Java Files](#day-1)

---

## Day 1: Project Setup & First Java Files <a name="day-1"></a>
**Date:** February 23, 2026

### What I built
- Created GitHub repository and set up project structure
- Created `Suit.java` — a clean enum representing the 4 card suits
- Created `Rank.java` — an enum where each rank carries its Blackjack value

### What I learned
- What enums are: a fixed set of named values, perfect when options never change
- What fields are; variables that belong to an object
- That Java enums are actually objects; they can hold data and constructors just like classes
- Java naming conventions: classes start Uppercase, fields and variables start lowercase
- Thinking of a field and constructor like a locker: the field is the locker, the constructor puts something inside it at creation
- Enum values act as a bijection; each name maps to exactly one value

### What's next (Really short-term)
- Build `Card.java` — first real class with fields, constructor, and methods
- Build `Deck.java` and `Hand.java`
- Configure the readme file [Temporal]

---

