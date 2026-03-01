package game;

enum Suit {
    Hearts("H"), Diamonds("D"), Clubs("C"), Spades("S");

    private String suit;
    Suit(String suit)
    {
        this.suit = suit;
    }

    public String getSuit() 
    {
        return suit;
    }
}

