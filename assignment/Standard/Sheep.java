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
        int dx = 0, dy = 0;

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
                if (rand.nextBoolean()) {
                    dx = 1;
                } else {
                    dx = -1;
                }
            } else {
                if (rand.nextBoolean()) {
                    dy = 1;
                } else {
                    dy = -1;
                }
            }
        }

        if ((dx != 0 || dy != 0) && (dx == 0 || dy == 0)) {
            int newX = x + dx;
            int newY = y + dy;

            if (newX >= 0 && newX < farm.getWidth() && newY >= 0 && newY < farm.getHeight()) {
                Cell particularCell = farm.getGrid()[newY][newX];

                synchronized (particularCell) {
                    if (particularCell instanceof EmptyCell || particularCell instanceof GateCell) {
                        synchronized (farm.getGrid()[y][x]) {
                            farm.getGrid()[y][x] = new EmptyCell(x, y);
                        }
                        setX(newX);
                        setY(newY);
                        farm.getGrid()[newY][newX] = this;

                        if (particularCell instanceof GateCell) {
                            System.out.println(name + " escaped the farm!");
                            farm.displayFarm();
                            ex.shutdown();
                        }
                    }
                }
            }
        }
    }
}
