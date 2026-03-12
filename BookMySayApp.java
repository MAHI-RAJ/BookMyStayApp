
import java.util.HashMap;
import java.util.Map;

/**
 * Manages the availability of different room types using a centralized HashMap.
 * Provides O(1) lookup and update complexity.
 */
public class RoomInventory {
    // Key: Room Type Name, Value: Number of available rooms
    private Map<String, Integer> inventory;

    public RoomInventory() {
        this.inventory = new HashMap<>();
    }

    /**
     * Registers a room type and sets its initial stock.
     */
    public void addRoomType(String roomType, int initialCount) {
        inventory.put(roomType, initialCount);
    }

    /**
     * Retrieves current availability for a specific room type.
     * Uses getOrDefault to prevent NullPointerExceptions if a type doesn't exist.
     */
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    /**
     * Updates availability (e.g., after a booking or cancellation).
     * @return true if update was successful, false if insufficient stock.
     */
    public boolean updateAvailability(String roomType, int change) {
        int current = getAvailability(roomType);
        if (current + change < 0) {
            return false; // Cannot have negative rooms
        }
        inventory.put(roomType, current + change);
        return true;
    }

    public void displayFullInventory() {
        System.out.println("\n--- Current Inventory Status ---");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.printf("%-15s : %d rooms available\n", entry.getKey(), entry.getValue());
        }
    }
}

public class HotelBookingApp {
    public static void main(String[] args) {
        System.out.println("=== Hotel Booking Management System v1.2 ===\n");

        // Step 1: Initialize the Centralized Inventory
        RoomInventory inventoryManager = new RoomInventory();

        // Step 2: Register Room Types (Scalable!)
        inventoryManager.addRoomType("Single Room", 10);
        inventoryManager.addRoomType("Double Room", 5);
        inventoryManager.addRoomType("Suite Room", 2);

        // Step 3: Simulate a booking
        System.out.println("Booking 1 Single Room...");
        if(inventoryManager.updateAvailability("Single Room", -1)) {
            System.out.println("Booking successful!");
        }

        // Step 4: Display results
        inventoryManager.displayFullInventory();

        // Example of O(1) Lookup
        System.out.println("\nQuick Check - Suites left: " + inventoryManager.getAvailability("Suite Room"));
      
 }