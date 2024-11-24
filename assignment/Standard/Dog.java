import java.util.concurrent.ThreadLocalRandom;

public class Dog extends Creature {
    public Dog(String name, int x, int y, Farm farm) {
        super(name, x, y, farm);
    }

    @Override
    public void simulate() {
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        int x = getX();
        int y = getY();

        boolean moved = false;
        while (!moved) {
            int dx, dy;
            do {
                dx = rand.nextInt(-1, 2);
                dy = rand.nextInt(-1, 2);
            } while ((dx == 0 && dy == 0) || (dx != 0 && dy != 0));

            int newX = x + dx;
            int newY = y + dy;

            if (newX >= 0 && newX < farm.getWidth() && newY >= 0 && newY < farm.getHeight()) {
                Cell particularCell = farm.getGrid()[newY][newX];

                synchronized (particularCell) {
                    if (particularCell instanceof EmptyCell) {
                        synchronized (farm.getGrid()[y][x]) {
                            farm.getGrid()[y][x] = new EmptyCell(x, y);
                        }
                        setX(newX);
                        setY(newY);
                        farm.getGrid()[newY][newX] = this;
                        moved = true;
                    }                 }
            }
        }
    }
}
