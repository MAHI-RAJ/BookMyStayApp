import java.util.*;

/**
 * Service responsible for rolling back state when a booking is cancelled.
 * Uses a Stack to manage released Room IDs for reuse.
 */
public class CancellationService {
    private RoomInventory inventory;
    private BookingHistoryService history;

    // Stack to keep track of recently released room IDs (LIFO)
    private Stack<String> releasedRoomIDs;

    public CancellationService(RoomInventory inventory, BookingHistoryService history) {
        this.inventory = inventory;
        this.history = history;
        this.releasedRoomIDs = new Stack<>();
    }

    /**
     * Performs a controlled rollback of a booking.
     * @param guestName Name of the guest cancelling
     * @param roomType The type of room being returned
     * @param roomID The specific ID being released
     */
    public void cancelBooking(String guestName, String roomType, String roomID) {
        System.out.println("\n--- Initiating Cancellation for: " + guestName + " ---");

        // 1. Validation: In a real system, you'd check if this booking exists in History/DB

        // 2. Increment Inventory (Inventory Restoration)
        inventory.updateAvailability(roomType, 1);

        // 3. Push the Room ID to the Stack (State Reversal)
        releasedRoomIDs.push(roomID);

        System.out.println("SUCCESS: Room " + roomID + " returned to " + roomType + " inventory.");
        System.out.println("Current Stack of available IDs for reuse: " + releasedRoomIDs);
    }

    public String getRecentlyReleasedID() {
        return releasedRoomIDs.isEmpty() ? null : releasedRoomIDs.pop();
    }
}

public class HotelBookingApp {
    public static void main(String[] args) {
        System.out.println("=== Hotel Booking Management System v1.9 ===\n");

        // Setup
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Suite Room", 1);

        BookingHistoryService history = new BookingHistoryService();
        BookingRequestQueue queue = new BookingRequestQueue();
        queue.enqueueRequest(new Reservation("Alice", "Suite Room"));

        BookingService bookingService = new BookingService(inventory, queue);

        // 1. Process Booking
        bookingService.processAllRequests(history);
        System.out.println("Inventory after booking: " + inventory.getAvailability("Suite Room"));

        // 2. Initialize Cancellation Service
        CancellationService cancellationService = new CancellationService(inventory, history);

        // 3. Alice cancels her booking
        // In a real app, these values would be retrieved from the history/guest record
        cancellationService.cancelBooking("Alice", "Suite Room", "S-101");

        // 4. Verify Inventory Consistency
        System.out.println("Inventory after cancellation: " + inventory.getAvailability("Suite Room"));
    }
}