import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Organizer implements Runnable {

    private final Dish dish;
    private Random rnd = new Random();
    private final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    public Organizer(Dish dish) {
        Objects.requireNonNull(dish);
        this.dish = dish;
    }

    @Override
    public void run() {
        Integer plate;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                plate = dish.extractFromDryTray();
            } catch (InterruptedException e) {
                System.out.printf("%s - Organizer has been interrupted while extracting from dry tray\n", LocalTime.now().format(dateTimeFormatter));
                return;
            }
            try {
                putInCupboard(plate);
            } catch (InterruptedException e) {
                System.out.printf("%s - Organizer has been interrupted while putting the dish in the cupboard\n", LocalTime.now().format(dateTimeFormatter));
                return;
            }
        }
        System.out.println("Organizer has been interrupted");
    }

    private void putInCupboard(int dish) throws InterruptedException {
        int time = rnd.nextInt(2)+1;
        System.out.printf("%s - Organizer is putting the dish in the cupboard %d\n", LocalTime.now().format(dateTimeFormatter), dish);
        TimeUnit.SECONDS.sleep(time);
    }
}
