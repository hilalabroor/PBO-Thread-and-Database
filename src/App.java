public class App {
    public static void main(String[] args) {
        System.out.println("\n\t=== SISTEM PESANAN CAFE ===");
        // Membuat thread pesanan
        AddOrderThread t1 = new AddOrderThread("Hilal", "Kopi Hitam", 2);
        AddOrderThread t2 = new AddOrderThread("Ijat", "Teh Tarik", 1);
        AddOrderThread t3 = new AddOrderThread("Abdi", "Cappuccino", 3);

        // Thread pemantau pesanan
        ShowOrdersThread monitor = new ShowOrdersThread();

        // Menjalankan semua thread
        t1.start();
        t2.start();
        t3.start();
        monitor.start();
    }
}
