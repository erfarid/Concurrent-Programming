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
        int dx = 0;
        int dy = 0;

        synchronized (farm) {
            
            boolean moved = false;
            while (!moved) {
                
                do {
                    dx = rand.nextInt(-1, 2);
                    dy = rand.nextInt(-1, 2);
                } while ((dx == 0 && dy == 0) || (dx != 0 && dy != 0) || (x + dx >= farm.getWidth() / 3 && x + dx < 2 * farm.getWidth() / 3
                        && y + dy >= farm.getHeight() / 3 && y + dy < 2 * farm.getHeight() / 3));

                if (x + dx >= 0 && x + dx < farm.getWidth() && y + dy >= 0 && y + dy < farm.getHeight()) {
                    if (farm.getGrid()[y + dy][x + dx] instanceof EmptyCell) {
                        farm.getGrid()[y][x] = new EmptyCell(x, y);
                        setX(x + dx);
                        setY(y + dy);
                        farm.getGrid()[y + dy][x + dx] = this;
                        moved = true;
                    }
                }
            }
        }
    }
}
