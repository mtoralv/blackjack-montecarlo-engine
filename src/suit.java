
enum Suit {
    Hearts("♥"), Diamonds("♦"), Clubs("♣"), Spades("♠");

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

