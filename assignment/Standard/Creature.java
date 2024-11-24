import java.util.concurrent.ExecutorService;

public abstract class Creature extends Cell implements Runnable {

    protected final Farm farm;
    protected ExecutorService ex;

    public Creature(String name, int x, int y, Farm farm) {
        super(name, x, y);
        this.farm = farm;
    }

    public void setExecutorService(ExecutorService ex) { this.ex = ex; }
    public abstract void simulate();

    @Override
    public void run() {
        while (!ex.isShutdown()) {
            try {
                simulate();
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    protected Farm getFarm() { return farm; }
}
