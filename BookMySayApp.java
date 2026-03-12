import java.util.*;

/**
 * Service responsible for processing queued requests and ensuring
 * unique room allocation without double-booking.
 */
public class BookingService {
    private RoomInventory inventory;
    private BookingRequestQueue requestQueue;

    // Maps Room Type to a Set of assigned Room IDs to ensure uniqueness
    // Example: "Suite Room" -> {"SR-101", "SR-102"}
    private Map<String, Set<String>> allocatedRooms;

    public BookingService(RoomInventory inventory, BookingRequestQueue requestQueue) {
        this.inventory = inventory;
        this.requestQueue = requestQueue;
        this.allocatedRooms = new HashMap<>();
    }

    public void processAllRequests() {
        System.out.println("\n--- Processing Booking Requests ---");

        while (!requestQueue.isEmpty()) {
            Reservation request = requestQueue.dequeueNextRequest();
            String type = request.getRequestedRoomType();

            // 1. Check Inventory
            if (inventory.getAvailability(type) > 0) {
                // 2. Generate Unique Room ID
                String roomID = generateRoomID(type);

                // 3. Allocate and Update Inventory
                allocateRoom(type, roomID);
                inventory.updateAvailability(type, -1);

                System.out.println("CONFIRMED: " + request.getGuestName() + " assigned to " + roomID);
            } else {
                System.out.println("FAILED: No availability for " + request.getGuestName() + " (" + type + ")");
            }
        }
    }

    private String generateRoomID(String type) {
        // Simple logic: Room Prefix + Current count in the set + 101
        String prefix = type.substring(0, 1).toUpperCase();
        int currentAllocated = allocatedRooms.getOrDefault(type, new HashSet<>()).size();
        return prefix + "-" + (101 + currentAllocated);
    }

    private void allocateRoom(String type, String roomID) {
        // Ensure the Set exists for this room type, then add the ID
        allocatedRooms.computeIfAbsent(type, k -> new HashSet<>()).add(roomID);
    }
}

public class HotelBookingApp {
    public static void main(String[] args) {
        System.out.println("=== Hotel Booking Management System v1.5 ===\n");

        // Setup Inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 2);
        inventory.addRoomType("Suite Room", 1);

        // Setup Request Queue
        BookingRequestQueue queue = new BookingRequestQueue();
        queue.enqueueRequest(new Reservation("Alice", "Suite Room"));
        queue.enqueueRequest(new Reservation("Bob", "Suite Room")); // Should fail (only 1 suite)
        queue.enqueueRequest(new Reservation("Charlie", "Single Room"));

        // Initialize Booking Service and Process
        BookingService bookingService = new BookingService(inventory, queue);
        bookingService.processAllRequests();

        // Final Inventory Check
        inventory.displayFullInventory();

    }
}