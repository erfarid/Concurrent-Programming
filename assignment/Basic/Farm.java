import java.util.*;
import java.util.concurrent.*;

public class Farm {

    private final int width;
    private final int height;
    private final Cell[][] grid;
    private final List<Sheep> sheepList = new ArrayList<>();
    private final List<Dog> dogList = new ArrayList<>();

    public Farm(int width, int height, int numberOfSheep, int numberOfDogs) {
        if (width % 3 != 2 || height % 3 != 2) {
            throw new IllegalArgumentException("Dimensions must be a multiple of three plus two.");
        }

        this.width = width;
        this.height = height;
        this.grid = new Cell[height][width];
        initializeFarm(numberOfSheep, numberOfDogs);
    }

    public List<Dog> getDogList() {
        return dogList;
    }

    public List<Sheep> getSheepList() {
        return sheepList;
    }

    public synchronized Cell[][] getGrid() {
        return grid;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getSize() {
        return Math.max(width, height);
    }

    private void initializeFarm(int numberOfSheep, int numberOfDogs) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i == 0 || j == 0 || i == height - 1 || j == width - 1) {
                    grid[i][j] = new WallCell(i, j);
                } else {
                    grid[i][j] = new EmptyCell(i, j);
                }
            }
        }

        placeGates();
        placeSheepAndDogs(numberOfSheep, numberOfDogs);
    }

    private void placeGates() {
        int topSide = ThreadLocalRandom.current().nextInt(width - 2) + 1;
        int bottomSide = ThreadLocalRandom.current().nextInt(width - 2) + 1;
        int leftSide = ThreadLocalRandom.current().nextInt(height - 2) + 1;
        int rightSide = ThreadLocalRandom.current().nextInt(height - 2) + 1;

        grid[0][topSide] = new GateCell(0, topSide);
        grid[height - 1][bottomSide] = new GateCell(height - 1, bottomSide);
        grid[leftSide][0] = new GateCell(leftSide, 0);
        grid[rightSide][width - 1] = new GateCell(rightSide, width - 1);
    }

    private void placeSheepAndDogs(int numberOfSheep, int numberOfDogs) {

        for (int i = 0; i < numberOfSheep; i++) {
            int x, y;
            do {
                x = ThreadLocalRandom.current().nextInt(width - 2) + 1;
                y = ThreadLocalRandom.current().nextInt(height - 2) + 1;
            } while (!isCenteredZone(x, y) || !(grid[y][x] instanceof EmptyCell));
            Sheep sheep = new Sheep(String.valueOf((char) ('A' + i)), x, y, this);
            sheepList.add(sheep);
            grid[y][x] = sheep;
        }

        for (int i = 0; i < numberOfDogs; i++) {
            int x, y;
            do {
                x = ThreadLocalRandom.current().nextInt(width - 2) + 1;
                y = ThreadLocalRandom.current().nextInt(height - 2) + 1;
            } while (isCenteredZone(x, y) || !(grid[y][x] instanceof EmptyCell));
            Dog dog = new Dog(String.valueOf(i + 1), x, y, this);
            dogList.add(dog);
            grid[y][x] = dog;
        }
    }

    private boolean isCenteredZone(int x, int y) {
        int zoneWidth = (width - 2) / 3;
        int zoneHeight = (height - 2) / 3;

        int startX = zoneWidth + 1;
        int endX = 2 * zoneWidth;
        int startY = zoneHeight + 1;
        int endY = 2 * zoneHeight;

        return x >= startX && x <= endX && y >= startY && y <= endY;
    }

    public synchronized void displayFarm() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.print("\u001B[0;0H");
        
    

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }

}
