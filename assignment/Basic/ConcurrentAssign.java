import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConcurrentAssign {

    public static void main(String[] args) throws Exception {
        int numberOfDogs = 5;
        int numberOfSheep = 10;
        
        if (numberOfSheep > 16) {
            System.out.println("The number of sheep cannot exceed 16, as there are only 16 cells available in the center zone.");
            return;  
        }
        if (numberOfDogs > 16) {
            System.out.println("Please provide bit less number of dogs ");
            return;  
        }

        Farm farm = new Farm(14, 14, numberOfSheep, numberOfDogs);
        farm.displayFarm();

        ExecutorService ex = Executors.newFixedThreadPool(numberOfSheep + numberOfDogs);
        var dogs = farm.getDogList();
        var sheep = farm.getSheepList();

        for (Dog dog : dogs) {
            dog.setExecutorService(ex);

            ex.execute(dog);
        }

        for (Sheep shp : sheep) {
            shp.setExecutorService(ex);

            ex.execute(shp);
        }

        while (!ex.isShutdown()) {
            
            farm.displayFarm();
            
            Thread.sleep(200);
        }
        

        if (!ex.awaitTermination(3, TimeUnit.SECONDS)) {
            ex.shutdownNow(); 
        }

        System.out.println("Congratulations:Game Ended as the sheep is Escaped you can see above !");

    }
}
