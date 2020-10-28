import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Dish {

    private static final int TRAY_CAPACITY = 10;

    private final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern("HH:mm:ss");
    private final ArrayList<Integer> cleanTray = new ArrayList<>();
    private final ArrayList<Integer> dryTray = new ArrayList<>();
    private final ReentrantLock lock = new ReentrantLock(true);
    private final Condition isNotFull = lock.newCondition();
    private final Condition isNotEmpty = lock.newCondition();

    public void addToCleanTray(Integer dish) throws InterruptedException {
        lock.lock();
        try {
            while (cleanTray.size() >= TRAY_CAPACITY) {
                System.out.printf("%s - Dish Washer waiting for the dish clean tray to have room\n", LocalTime.now().format(dateTimeFormatter));
                isNotFull.await();
            }
            cleanTray.add(dish);
            System.out.printf("%s - Dish Washer puts dish %d on the clean tray\n", LocalTime.now().format(dateTimeFormatter), dish);
            isNotEmpty.signal();
        } finally {
            lock.unlock();
        }

    }

    public Integer extractFromCleanTray() throws InterruptedException {
        Integer dish;
        lock.lock();

        try {
            while (cleanTray.isEmpty()) {
                System.out.printf("%s - Dish Dryer waiting for the clean tray to have a clean dish\n", LocalTime.now().format(dateTimeFormatter));
                isNotEmpty.await();
            }
            dish = cleanTray.remove(0);
            System.out.printf("%s - Dish Dryer extracts clean dish %d from clean tray\n", LocalTime.now().format(dateTimeFormatter), dish);
            isNotFull.signal();
            return dish;
        } finally {
            lock.unlock();
        }

    }

    public void addToDryTray(Integer dish) throws InterruptedException {
        lock.lock();

        try {
            while (dryTray.size() >= TRAY_CAPACITY) {
                System.out.printf("%s - Dish Dryer waiting for the dish dry tray to have room\n", LocalTime.now().format(dateTimeFormatter));
                isNotFull.await();
            }
            dryTray.add(dish);
            System.out.printf("%s - Dish Dryer puts dry dish %d on the dry tray\n", LocalTime.now().format(dateTimeFormatter), dish);
            isNotEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public Integer extractFromDryTray() throws InterruptedException {
        Integer dish;

        lock.lock();

        try {
            while (dryTray.isEmpty()) {
                System.out.printf("%s - Organizer waiting for the dry tray to have a dry dish\n", LocalTime.now().format(dateTimeFormatter));
                isNotEmpty.await();
            }
            dish = dryTray.remove(0);
            System.out.printf("%s - Organizer extracts clean dish %d from clean tray and put it in the cupboard\n", LocalTime.now().format(dateTimeFormatter), dish);
            isNotFull.signal();
            return dish;
        } finally {
            lock.unlock();
        }
    }
}
