/**
 * Custom exception for domain-specific booking errors.
 */
public class BookingException extends Exception {
    public BookingException(String message) {
        super(message);
    }
}

/**
 * Ensures that booking requests are valid before they reach the core logic.
 */
public class BookingValidator {

    public static void validateRequest(Reservation request, RoomInventory inventory) throws BookingException {
        // 1. Validate Input
        if (request.getGuestName() == null || request.getGuestName().trim().isEmpty()) {
            throw new BookingException("Invalid Guest Name: Name cannot be empty.");
        }

        // 2. Validate Room Type Existence
        int currentStock = inventory.getAvailability(request.getRequestedRoomType());
        if (currentStock == -1) { // Assuming -1 means type doesn't exist in map
            throw new BookingException("Room Type Error: '" + request.getRequestedRoomType() + "' does not exist.");
        }

        // 3. Validate Inventory Availability (Fail-Fast)
        if (currentStock <= 0) {
            throw new BookingException("Inventory Error: No " + request.getRequestedRoomType() + "s available.");
        }
    }
}

public void processAllRequests(BookingHistoryService history) {
    System.out.println("\n--- Processing Requests with Validation ---");

    while (!requestQueue.isEmpty()) {
        Reservation request = requestQueue.dequeueNextRequest();

        try {
            // Validate before any mutation happens
            BookingValidator.validateRequest(request, inventory);

            // If we reach here, validation passed
            String roomID = generateRoomID(request.getRequestedRoomType());
            inventory.updateAvailability(request.getRequestedRoomType(), -1);
            history.recordBooking(request.getGuestName(), roomID);

            System.out.println("SUCCESS: Confirmed " + request.getGuestName());

        } catch (BookingException e) {
            // Catching the error allows the loop to continue for the next guest
            System.err.println("VALIDATION FAILED: " + e.getMessage());

        }
    }
}

public class HotelBookingApp {
    public static void main(String[] args) {
        System.out.println("=== Hotel Booking Management System v1.8 ===\n");

        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 1);

        BookingRequestQueue queue = new BookingRequestQueue();

        // Scenario A: Valid request
        queue.enqueueRequest(new Reservation("Alice", "Single Room"));

        // Scenario B: Room type doesn't exist (should trigger error)
        queue.enqueueRequest(new Reservation("Bob", "Penthouse"));

        // Scenario C: Empty name (should trigger error)
        queue.enqueueRequest(new Reservation("", "Single Room"));

        BookingService service = new BookingService(inventory, queue);
        BookingHistoryService history = new BookingHistoryService();

        service.processAllRequests(history);

        System.out.println("\nSystem remains stable after processing errors.");
    }
}