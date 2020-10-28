import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class DishWasher implements Runnable {

    private final Dish dish;
    private int dishNumber;
    private Random rnd = new Random();
    private final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    public DishWasher(Dish dish) {
            Objects.requireNonNull(dish);
            this.dish = dish;
    }

    @Override
    public void run() {
        Integer plate;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                plate = cleanDirtyDish();
            } catch (InterruptedException e) {
                System.out.printf("%s - Dish Washer has been interrupted while cleaning a dirty dish\n", LocalTime.now().format(dateTimeFormatter));
                return;
            }
            try {
                dish.addToCleanTray(plate);
            } catch (InterruptedException e) {
                System.out.printf("%s - Dish Washer has been interrupted while adding a dish to the clean tray", LocalTime.now().format(dateTimeFormatter));
                return;
            }
        }
        System.out.printf("%s - Dish Washer has been interrupted\n", LocalTime.now().format(dateTimeFormatter));
    }

    private int cleanDirtyDish() throws InterruptedException {
        Integer dish = ++dishNumber;
        int time = rnd.nextInt(8-4+1)+4;
        System.out.printf("%s - Dish Washer is cleaning a dirty dish %d\n", LocalTime.now().format(dateTimeFormatter), dish);
        TimeUnit.SECONDS.sleep(time);
        return dish;
    }
}
