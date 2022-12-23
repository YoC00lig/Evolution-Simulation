package agh.ics.oop;

public class ToxicMap extends AbstractWorldMap{
    public ToxicMap(int width, int height, boolean predistination, boolean isCrazyMode, boolean hellExistsMode, int reproductionE, int plantE, int initialE) {
        super(width, height, predistination, isCrazyMode, hellExistsMode, reproductionE, plantE, initialE);
    }

    @Override
    public void plantGrass() {
        int ans = (int) (Math.random() * 10);
            this.informations.sort(new ComparatorForDeaths());
            int breakIndex = (this.informations.size()*2)/10;
            switch (ans) {
                case 0,1 ->  plantGrassInFieldFromGivenRange(breakIndex, this.informations.size());
                default -> plantGrassInFieldFromGivenRange(0, breakIndex);
            }
        }
    }

