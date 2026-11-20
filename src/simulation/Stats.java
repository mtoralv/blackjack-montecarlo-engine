package simulation;

import game.RoundResult;

public final class Stats {

    static final int OUTCOME_OFFSET = 16;
    static final int OUTCOME_BUCKETS = 2 * OUTCOME_OFFSET + 1;
    static final int TC_MIN = -10;
    static final int TC_MAX = 10;

    long rounds;
    long wins;
    long losses;
    long pushes;
    long playerBlackjacks;
    long dealerBlackjacks;
    long doubles;
    long splits;
    long surrenders;

    double sumBet;
    double sumBet2;
    double sumWagered;
    double sumNet;
    double sumNet2;
    double sumNetBet;
    double sumR; // r = net / bet, so the bet size doesn't matter
    double sumR2;

    final long[] outcomes = new long[OUTCOME_BUCKETS];
    final long[] tcRounds = new long[TC_MAX - TC_MIN + 1];
    final double[] tcSumR = new double[TC_MAX - TC_MIN + 1];
    final double[] tcSumR2 = new double[TC_MAX - TC_MIN + 1];

    void record(RoundResult r, double trueCount) {
        rounds++;
        double net = r.net();
        double bet = r.initialBet();
        if (net > 0) wins++;
        else if (net < 0) losses++;
        else pushes++;
        if (r.playerBlackjack()) playerBlackjacks++;
        if (r.dealerBlackjack()) dealerBlackjacks++;
        doubles += r.doubles();
        splits += r.splits();
        if (r.surrendered()) surrenders++;

        sumBet += bet;
        sumBet2 += bet * bet;
        sumWagered += r.totalWagered();
        sumNet += net;
        sumNet2 += net * net;
        sumNetBet += net * bet;
        double ratio = net / bet;
        sumR += ratio;
        sumR2 += ratio * ratio;

        int bucket = (int) Math.round(ratio * 2) + OUTCOME_OFFSET;
        outcomes[Math.max(0, Math.min(OUTCOME_BUCKETS - 1, bucket))]++;

        int tc = (int) Math.max(TC_MIN, Math.min(TC_MAX, Math.floor(trueCount)));
        tcRounds[tc - TC_MIN]++;
        tcSumR[tc - TC_MIN] += ratio;
        tcSumR2[tc - TC_MIN] += ratio * ratio;
    }

    void merge(Stats o) {
        rounds += o.rounds;
        wins += o.wins;
        losses += o.losses;
        pushes += o.pushes;
        playerBlackjacks += o.playerBlackjacks;
        dealerBlackjacks += o.dealerBlackjacks;
        doubles += o.doubles;
        splits += o.splits;
        surrenders += o.surrenders;
        sumBet += o.sumBet;
        sumBet2 += o.sumBet2;
        sumWagered += o.sumWagered;
        sumNet += o.sumNet;
        sumNet2 += o.sumNet2;
        sumNetBet += o.sumNetBet;
        sumR += o.sumR;
        sumR2 += o.sumR2;
        for (int i = 0; i < outcomes.length; i++) outcomes[i] += o.outcomes[i];
        for (int i = 0; i < tcRounds.length; i++) {
            tcRounds[i] += o.tcRounds[i];
            tcSumR[i] += o.tcSumR[i];
            tcSumR2[i] += o.tcSumR2[i];
        }
    }

    // usual house edge (per initial bet)
    public double houseEdge() {
        return -sumR / rounds;
    }

    public double houseEdgeStdError() {
        double mean = sumR / rounds;
        double var = Math.max(0, sumR2 / rounds - mean * mean);
        return Math.sqrt(var / rounds);
    }

    // total net / total bet, this is what bet sizing can change
    public double returnOnAction() {
        return sumNet / sumBet;
    }

    // delta method for the ratio estimator
    public double returnOnActionStdError() {
        double ratio = returnOnAction();
        double meanBet = sumBet / rounds;
        double meanSq = (sumNet2 - 2 * ratio * sumNetBet + ratio * ratio * sumBet2) / rounds;
        return Math.sqrt(Math.max(0, meanSq) / rounds) / meanBet;
    }

    public double meanNet() {
        return sumNet / rounds;
    }

    public double stdDevNet() {
        double mean = meanNet();
        return Math.sqrt(Math.max(0, sumNet2 / rounds - mean * mean));
    }

    public double meanBet() {
        return sumBet / rounds;
    }

    public long rounds() {
        return rounds;
    }

    public double rate(long count) {
        return (double) count / rounds;
    }
}
