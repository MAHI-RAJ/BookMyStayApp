import java.util.ArrayList;
import java.util.List;

/**
 * Maintains a historical record of all confirmed reservations.
 * Provides reporting capabilities for administrators.
 */
public class BookingHistoryService {
    // A List is perfect for a chronological audit trail
    private List<String> confirmedBookings;

    public BookingHistoryService() {
        this.confirmedBookings = new ArrayList<>();
    }

    /**
     * Records a successful transaction into history.
     */
    public void recordBooking(String guestName, String roomID) {
        String record = String.format("Guest: %-10s | Room: %-10s", guestName, roomID);
        confirmedBookings.add(record);
    }

    /**
     * Generates a summary report for the Admin.
     * This is a read-only operation.
     */
    public void generateFullReport() {
        System.out.println("\n========= ADMINISTRATIVE BOOKING REPORT =========");
        if (confirmedBookings.isEmpty()) {
            System.out.println("No history found for the current session.");
        } else {
            System.out.println("Total Confirmed Bookings: " + confirmedBookings.size());
            System.out.println("-------------------------------------------------");
            for (int i = 0; i < confirmedBookings.size(); i++) {
                System.out.println((i + 1) + ". " + confirmedBookings.get(i));
            }
        }
        System.out.println("=================================================");
    }
}

// Inside BookingService.java (Modified)
public void processAllRequests(BookingHistoryService history) {
    while (!requestQueue.isEmpty()) {
        Reservation request = requestQueue.dequeueNextRequest();
        String type = request.getRequestedRoomType();

        if (inventory.getAvailability(type) > 0) {
            String roomID = generateRoomID(type);
            allocateRoom(type, roomID);
            inventory.updateAvailability(type, -1);

            // NEW: Record the event in history for reporting
            history.recordBooking(request.getGuestName(), roomID);

            System.out.println("CONFIRMED: " + request.getGuestName() + " -> " + roomID);
        } else {
            System.out.println("FAILED: No stock for " + request.getGuestName());
        }
    }
}

public class HotelBookingApp {
    public static void main(String[] args) {
        System.out.println("=== Hotel Booking Management System v1.7 ===\n");

        // 1. Setup
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Suite Room", 2);

        BookingRequestQueue queue = new BookingRequestQueue();
        queue.enqueueRequest(new Reservation("Alice", "Suite Room"));
        queue.enqueueRequest(new Reservation("Bob", "Suite Room"));

        BookingHistoryService history = new BookingHistoryService();
        BookingService bookingService = new BookingService(inventory, queue);

        // 2. Process
        bookingService.processAllRequests(history);

        // 3. Admin generates report at the end of the day
        history.generateFullReport();
    }
}