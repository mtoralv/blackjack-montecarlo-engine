public interface Strategy {
    String decide(Hand playerHand, Card dealerUpcard);
}