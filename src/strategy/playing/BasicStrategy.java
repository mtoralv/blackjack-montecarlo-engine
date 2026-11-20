package strategy.playing;

import game.Action;
import game.Card;
import game.Hand;
import game.Options;
import game.Rules;

public final class BasicStrategy implements PlayingStrategy {

    // H hit, S stand, D double (else hit), X double (else stand), P split,
    // R surrender (else hit), Q surrender (else stand)
    // columns = dealer card: 2 3 4 5 6 7 8 9 T A
    private static final String[] HARD_S17 = new String[22];
    private static final String[] SOFT_S17 = new String[22];
    private static final String[] PAIRS = new String[12];

    static {
        for (int t = 4; t <= 8; t++) HARD_S17[t] = "HHHHHHHHHH";
        HARD_S17[9]  = "HDDDDHHHHH";
        HARD_S17[10] = "DDDDDDDDHH";
        HARD_S17[11] = "DDDDDDDDDH";
        HARD_S17[12] = "HHSSSHHHHH";
        HARD_S17[13] = "SSSSSHHHHH";
        HARD_S17[14] = "SSSSSHHHHH";
        HARD_S17[15] = "SSSSSHHHRH";
        HARD_S17[16] = "SSSSSHHRRR";
        for (int t = 17; t <= 21; t++) HARD_S17[t] = "SSSSSSSSSS";

        SOFT_S17[12] = "HHHHHHHHHH"; // A,A when you can't split anymore
        SOFT_S17[13] = "HHHDDHHHHH";
        SOFT_S17[14] = "HHHDDHHHHH";
        SOFT_S17[15] = "HHDDDHHHHH";
        SOFT_S17[16] = "HHDDDHHHHH";
        SOFT_S17[17] = "HDDDDHHHHH";
        SOFT_S17[18] = "SXXXXSSHHH";
        for (int t = 19; t <= 21; t++) SOFT_S17[t] = "SSSSSSSSSS";

        PAIRS[2]  = "PPPPPPHHHH";
        PAIRS[3]  = "PPPPPPHHHH";
        PAIRS[4]  = "HHHPPHHHHH";
        PAIRS[6]  = "PPPPPHHHHH";
        PAIRS[7]  = "PPPPPPHHHH";
        PAIRS[8]  = "PPPPPPPPPP";
        PAIRS[9]  = "PPPPPSPPSS";
        PAIRS[11] = "PPPPPPPPPP";
        // 5,5 and 10,10 are never split, they go to the hard table
    }

    private final String[] hard;
    private final String[] soft;

    public BasicStrategy(Rules rules) {
        this.hard = HARD_S17.clone();
        this.soft = SOFT_S17.clone();
        if (rules.dealerHitsSoft17()) { // H17 changes
            hard[11] = "DDDDDDDDDD";
            hard[15] = "SSSSSHHHRR";
            hard[17] = "SSSSSSSSSQ";
            soft[18] = "XXXXXSSHHH";
            soft[19] = "SSSSXSSSSS";
        }
    }

    @Override
    public Action decide(Hand hand, Card dealerUpcard, Options options) {
        int col = dealerUpcard.value() - 2; // ace is 11 so it ends up in the last column
        if (options.canSplit() && hand.isPair()) {
            String row = PAIRS[hand.card(0).value()];
            if (row != null && row.charAt(col) == 'P') {
                return Action.SPLIT;
            }
        }
        String row = hand.isSoft() ? soft[hand.total()] : hard[hand.total()];
        return switch (row.charAt(col)) {
            case 'H' -> Action.HIT;
            case 'S' -> Action.STAND;
            case 'D' -> Action.DOUBLE_OR_HIT;
            case 'X' -> Action.DOUBLE_OR_STAND;
            case 'R' -> Action.SURRENDER_OR_HIT;
            case 'Q' -> Action.SURRENDER_OR_STAND;
            default -> throw new IllegalStateException("Bad chart entry");
        };
    }

    @Override
    public String name() {
        return "BasicStrategy";
    }
}
