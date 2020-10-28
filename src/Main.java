public class Main {

    public static void main(String[] args) throws InterruptedException {

        Dish dish = new Dish();
        Thread dishWasher = new Thread(new DishWasher(dish));
        Thread dishDryer = new Thread(new DishDryer(dish));
        Thread organizer = new Thread(new Organizer(dish));

        dishWasher.start();
        dishDryer.start();
        organizer.start();
        Thread.currentThread();
        Thread.sleep(60000);

        dishWasher.interrupt();
        dishDryer.interrupt();
        organizer.interrupt();

        System.out.println("Happy Birthday!");




    }
}
