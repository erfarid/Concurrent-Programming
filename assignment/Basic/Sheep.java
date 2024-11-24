import java.util.concurrent.ThreadLocalRandom;

public class Sheep extends Creature {

    public Sheep(String name, int x, int y, Farm farm) {
        super(name, x, y, farm);
    }

    @Override
    public void simulate() {
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        int x = getX();
        int y = getY();
        int dx = 0;
        int dy = 0;

        synchronized (farm) {

            if (x > 0 && farm.getGrid()[y][x - 1] instanceof Dog) {
                dx = 1;
            } else if (x < farm.getWidth() - 1 && farm.getGrid()[y][x + 1] instanceof Dog) {
                dx = -1;
            }

            if (y > 0 && farm.getGrid()[y - 1][x] instanceof Dog) {
                dy = 1;
            } else if (y < farm.getHeight() - 1 && farm.getGrid()[y + 1][x] instanceof Dog) {
                dy = -1;
            }

            if (dx == 0 && dy == 0) {
                if (rand.nextBoolean()) {
                    dx = rand.nextBoolean() ? 1 : -1;
                } else {
                    dy = rand.nextBoolean() ? 1 : -1;
                }
            }

            if ((dx != 0 || dy != 0) && (dx == 0 || dy == 0)) {
                Cell cell = (Cell) farm.getGrid()[y + dy][x + dx];
                if (cell instanceof EmptyCell || cell instanceof GateCell) {
                    farm.getGrid()[y][x] = new EmptyCell(x, y);
                    setX(x + dx);
                    setY(y + dy);
                    farm.getGrid()[y + dy][x + dx] = this;

                    if (cell instanceof GateCell) {
                        
                        System.out.println(name + " escaped the farm!");
                        farm.displayFarm();
                        ex.shutdown();
                    }
                    
                }
            }

        }

    }

}
