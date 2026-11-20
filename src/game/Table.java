package game;

import java.util.ArrayList;
import java.util.List;

import strategy.playing.PlayingStrategy;

public final class Table {

    private final Rules rules;
    private final Shoe shoe;

    public Table(Rules rules, Shoe shoe) {
        this.rules = rules;
        this.shoe = shoe;
    }

    public Rules rules() {
        return rules;
    }

    public void shuffleIfNeeded(CardListener listener) {
        if (shoe.needsShuffle()) {
            shoe.shuffle();
            listener.onShuffle();
        }
    }

    public RoundResult playRound(double bet, double funds, PlayingStrategy player, CardListener listener) {
        shuffleIfNeeded(listener);

        Hand first = new Hand();
        Hand dealer = new Hand();
        first.add(deal(listener, true));
        dealer.add(deal(listener, true));
        first.add(deal(listener, true));
        Card hole = deal(listener, false);
        dealer.add(hole);
        Card upcard = dealer.card(0);

        boolean dealerBlackjack = dealer.isBlackjack();
        boolean playerBlackjack = first.isBlackjack();

        // dealer checks for blackjack when showing an ace or a 10
        if (rules.dealerPeeks() && upcard.value() >= 10 && dealerBlackjack) {
            listener.onCard(hole);
            double net = playerBlackjack ? 0 : -bet;
            return new RoundResult(bet, bet, net, List.of(first), List.of(net), dealer, playerBlackjack, true, 0, 0, false);
        }
        if (playerBlackjack) {
            listener.onCard(hole);
            double net = dealerBlackjack ? 0 : bet * rules.blackjackPayout();
            return new RoundResult(bet, bet, net, List.of(first), List.of(net), dealer, true, dealerBlackjack, 0, 0, false);
        }

        List<Hand> hands = new ArrayList<>(rules.maxHands());
        List<Double> bets = new ArrayList<>(rules.maxHands());
        hands.add(first);
        bets.add(bet);
        int doubles = 0;
        int splits = 0;
        boolean surrendered = false;

        for (int i = 0; i < hands.size(); i++) {
            Hand hand = hands.get(i);
            if (hand.size() == 1) {
                hand.add(deal(listener, true)); // second card of a split hand
            }
            boolean splitAces = hand.isFromSplit() && hand.card(0).isAce();

            while (!hand.isBust() && hand.total() < 21) {
                if (splitAces && !(rules.resplitAces() && hand.isPair() && hands.size() < rules.maxHands())) {
                    break; // split aces only get one card
                }
                double committed = sum(bets);
                boolean twoCards = hand.size() == 2;
                boolean canDouble = twoCards && !splitAces
                        && (!hand.isFromSplit() || rules.doubleAfterSplit())
                        && funds >= committed + bets.get(i);
                boolean canSplit = hand.isPair() && hands.size() < rules.maxHands()
                        && (!hand.isFromSplit() || !hand.card(0).isAce() || rules.resplitAces())
                        && funds >= committed + bets.get(i);
                boolean canSurrender = rules.lateSurrender() && twoCards && hands.size() == 1 && !hand.isFromSplit();

                Action action = player.decide(hand, upcard, new Options(canDouble, canSplit, canSurrender, i, hands.size()));
                action = resolve(action, canDouble, canSplit, canSurrender);
                if (splitAces && action != Action.SPLIT) {
                    break;
                }

                if (action == Action.STAND) {
                    break;
                } else if (action == Action.HIT) {
                    hand.add(deal(listener, true));
                } else if (action == Action.DOUBLE_OR_HIT) {
                    bets.set(i, bets.get(i) * 2);
                    doubles++;
                    hand.add(deal(listener, true));
                    break;
                } else if (action == Action.SPLIT) {
                    splits++;
                    Hand second = Hand.splitFrom(hand.removeSecond());
                    hands.add(i + 1, second);
                    bets.add(i + 1, bets.get(i));
                    hand.add(deal(listener, true));
                    splitAces = hand.card(0).isAce();
                } else if (action == Action.SURRENDER_OR_HIT) {
                    surrendered = true;
                    break;
                }
            }
            if (surrendered) break;
        }

        listener.onCard(hole); // hole card is shown at the end so counters see it
        boolean anyLive = false;
        if (!surrendered) {
            for (Hand h : hands) {
                if (!h.isBust()) {
                    anyLive = true;
                    break;
                }
            }
        }
        // dealer only plays if there's some hand left that isn't bust
        if (anyLive && !(dealerBlackjack && !rules.dealerPeeks())) {
            while (dealerShouldHit(dealer)) {
                dealer.add(deal(listener, true));
            }
        }

        List<Double> nets = new ArrayList<>(hands.size());
        double net = 0;
        double wagered = 0;
        for (int i = 0; i < hands.size(); i++) {
            double b = bets.get(i);
            wagered += b;
            double handNet = surrendered ? -b / 2 : settle(hands.get(i), dealer, b, dealerBlackjack);
            nets.add(handNet);
            net += handNet;
        }
        return new RoundResult(bet, wagered, net, hands, nets, dealer, false, dealerBlackjack, doubles, splits, surrendered);
    }

    private double settle(Hand hand, Hand dealer, double bet, boolean dealerBlackjack) {
        if (hand.isBust()) return -bet;
        if (dealerBlackjack) return -bet; // only happens without peek
        if (dealer.isBust()) return bet;
        int p = hand.total();
        int d = dealer.total();
        if (p > d) return bet;
        if (p < d) return -bet;
        return 0;
    }

    private boolean dealerShouldHit(Hand dealer) {
        int total = dealer.total();
        return total < 17 || (total == 17 && dealer.isSoft() && rules.dealerHitsSoft17());
    }

    private static Action resolve(Action action, boolean canDouble, boolean canSplit, boolean canSurrender) {
        return switch (action) {
            case DOUBLE_OR_HIT -> canDouble ? Action.DOUBLE_OR_HIT : Action.HIT;
            case DOUBLE_OR_STAND -> canDouble ? Action.DOUBLE_OR_HIT : Action.STAND;
            case SURRENDER_OR_HIT -> canSurrender ? Action.SURRENDER_OR_HIT : Action.HIT;
            case SURRENDER_OR_STAND -> canSurrender ? Action.SURRENDER_OR_HIT : Action.STAND;
            case SPLIT -> canSplit ? Action.SPLIT : Action.HIT;
            default -> action;
        };
    }

    private Card deal(CardListener listener, boolean visible) {
        if (shoe.isEmpty()) {
            shoe.shuffle();
            listener.onShuffle();
        }
        Card card = shoe.draw();
        if (visible) listener.onCard(card);
        return card;
    }

    private static double sum(List<Double> values) {
        double s = 0;
        for (double v : values) s += v;
        return s;
    }
}
