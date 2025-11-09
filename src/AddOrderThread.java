import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddOrderThread extends Thread {
    private final String customer;
    private final String item;
    private final int qty;

    public AddOrderThread(String customer, String item, int qty) {
        this.customer = customer;
        this.item = item;
        this.qty = qty;
    }

    @Override
    public void run() {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                System.out.println("Koneksi database gagal!");
                return;
            }

            String sql = "INSERT INTO orders (customer_name, menu_item, quantity, status) VALUES (?, ?, ?, 'Sedang Diproses')";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, customer);
            ps.setString(2, item);
            ps.setInt(3, qty);
            ps.executeUpdate();

            System.out.println("[Thread 1] Pesanan disimpan dalam Database:\n " + "\t   --> " + customer + " - " + item);
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("Gagal menyimpan pesanan dalam Database: " + e.getMessage());
        }
    }
}
