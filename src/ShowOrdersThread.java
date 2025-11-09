import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ShowOrdersThread extends Thread {
    @Override
    public void run() {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                System.out.println("Koneksi database gagal!");
                return;
            }

            for (int i = 0; i < 5; i++) {
                System.out.println("\n[Thread 2] Daftar Pesanan Café:");
                System.out.println("-----------------------------------------------");
                String sql = "SELECT * FROM orders";
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    System.out.println("- " + rs.getString("customer_name") + " memesan " +
                            rs.getString("menu_item") + " x" + rs.getInt("quantity") +
                            " (" + rs.getString("status") + ")");
                }

                System.out.println("-----------------------------------------------");
                Thread.sleep(2000);
            }

            System.out.println("\n[Thread 2] Pemantauan Database selesai");
        } catch (Exception e) {
            System.out.println("Gagal menampilkan pesanan: " + e.getMessage());
        }
    }
}
