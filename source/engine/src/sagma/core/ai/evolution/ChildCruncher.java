package sagma.core.ai.evolution;

import sagma.core.data.generator.number.RandomNumberGenerator;

/**
 *
 * @author Aaron Kison
 */
class ChildCruncher extends Thread {
    private DNA best, secondBest;
    private int index, length;
    private boolean isReady = true;
    private DNA[] store;
    private RandomNumberGenerator rand100 = new RandomNumberGenerator(0, 100);

    public void setParents(DNA best, DNA secondBest) {
        this.best = best;
        this.secondBest = secondBest;
    }

    public void setPeriod(int start, int length) {
        index = start;
        this.length = length;
    }

    @Override
    public void run() {
        isReady = false;

        int bestLength = best.getDNA().length;
        int firstLength = bestLength;
        int secondLength = secondBest.getDNA().length;
        int[] path;

        for (int i = 0; i < length; i++) {
            if (firstLength > secondLength) path = new int[firstLength];
            else path = new int[secondLength];

            if (secondLength < bestLength) bestLength = secondLength;
            for (int j = 0; j < bestLength; j++) {
                if (rand100.nextNumber() > 50-Evolution.BEST_PARENT_BIAS) path[j] = best.getDNA()[j];
                else path[j] = secondBest.getDNA()[j];
            }

            if (firstLength > secondLength) System.arraycopy(best.getDNA(), secondLength, path, secondLength, firstLength-secondLength);
            else if (firstLength < secondLength) System.arraycopy(secondBest.getDNA(), firstLength, path, firstLength, secondLength-firstLength);

            path = Evolution.mutate(path);
            store[index+i] = new DNA(path);
        }

        isReady = true;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setStore(DNA[] pathStore) {
        store = pathStore;
    }

    void setIsBusy(boolean b) {
        isReady = !b;
    }
}

