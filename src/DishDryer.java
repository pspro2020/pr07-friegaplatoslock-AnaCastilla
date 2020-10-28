import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class DishDryer implements Runnable {

    private final Dish dish;
    private int dishNumber;
    private Random rnd = new Random();
    private final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    public DishDryer(Dish dish) {
        Objects.requireNonNull(dish);
        this.dish = dish;
    }

    @Override
    public void run() {
        Integer plate;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                plate = dish.extractFromCleanTray();
            } catch (InterruptedException e) {
                System.out.printf("%s - Dish Dryer has been interrupted while extracting from clean tray\n", LocalTime.now().format(dateTimeFormatter));
                return;
            }
            try {
                plate = dryCleanDish();
            } catch (InterruptedException e) {
                System.out.printf("%s - Dish Dryer has been interrupted while drying a clean dish\n", LocalTime.now().format(dateTimeFormatter));
                return;
            }
            try {
                dish.addToDryTray(plate);
            } catch (InterruptedException e) {
                System.out.printf("%s - Dish Dryer has been interrupted while adding a dish to the dry tray", LocalTime.now().format(dateTimeFormatter));
                return;
            }
        }
        System.out.printf("%s - Dish Dryer has been interrupted\n", LocalTime.now().format(dateTimeFormatter));
    }

    private int dryCleanDish() throws InterruptedException {
        Integer dish = ++dishNumber;
        int time = rnd.nextInt(3)+1;
        System.out.printf("%s - Dish Dryer is drying a clean dish %d\n", LocalTime.now().format(dateTimeFormatter), dish);
        TimeUnit.SECONDS.sleep(time);
        return dish;
    }
}
