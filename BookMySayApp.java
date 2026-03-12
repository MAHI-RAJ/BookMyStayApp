
import java.util.ArrayList;
import java.util.List;

/**
 * Simulates multiple users booking rooms simultaneously.
 * Uses synchronization to prevent race conditions.
 */
public class ConcurrentBookingProcessor {
    private RoomInventory inventory;
    private BookingRequestQueue requestQueue;
    private BookingHistoryService history;

    public ConcurrentBookingProcessor(RoomInventory inventory, BookingRequestQueue queue, BookingHistoryService history) {
        this.inventory = inventory;
        this.requestQueue = queue;
        this.history = history;
    }

    /**
     * The Critical Section: synchronized ensures only one thread
     * enters this block at a time.
     */
    public synchronized void processBookingSafely(Reservation request) {
        String type = request.getRequestedRoomType();

        // Step 1: Check (If another thread is here, it must wait)
        if (inventory.getAvailability(type) > 0) {

            // Artificial delay to simulate processing and expose race conditions
            try { Thread.sleep(10); } catch (InterruptedException e) {}

            // Step 2: Act (Decrement and Record)
            inventory.updateAvailability(type, -1);
            history.recordBooking(request.getGuestName(), "CONFIRMED-" + type);

            System.out.println("[Thread " + Thread.currentThread().getId() + "] SUCCESS: " + request.getGuestName());
        } else {
            System.out.println("[Thread " + Thread.currentThread().getId() + "] FAILED: No stock for " + request.getGuestName());
        }

    }
}

public class HotelBookingApp {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== UC11: Concurrent Booking Simulation ===\n");

        // 1. Setup shared resources (only 2 rooms available)
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Suite Room", 2);

        BookingHistoryService history = new BookingHistoryService();
        ConcurrentBookingProcessor processor = new ConcurrentBookingProcessor(inventory, null, history);

        // 2. Create 5 Guest Requests (Competing for 2 rooms)
        String[] guests = {"Alice", "Bob", "Charlie", "David", "Eve"};
        List<Thread> threads = new ArrayList<>();

        for (String name : guests) {
            Reservation req = new Reservation(name, "Suite Room");
            Thread t = new Thread(() -> {
                processor.processBookingSafely(req);
            });
            threads.add(t);
        }

        // 3. Start all threads "simultaneously"
        for (Thread t : threads) t.start();

        // 4. Wait for all threads to finish
        for (Thread t : threads) t.join();

        // 5. Verify consistency
        System.out.println("\nFinal Inventory Count: " + inventory.getAvailability("Suite Room"));
        history.generateFullReport();
    }
}