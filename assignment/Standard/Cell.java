public abstract class Cell {
    protected int x;
    protected int y;
    protected String name;

    public Cell(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public synchronized int getX() {
        return x;
    }

    public synchronized int getY() {
        return y;
    }

    public synchronized void setX(int x) {
        this.x = x;
    }

    public synchronized void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return name;
    }
}
