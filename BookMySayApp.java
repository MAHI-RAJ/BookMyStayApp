import java.util.List;
import java.util.ArrayList;

/**
 * Service responsible for read-only search operations.
 * It ensures that guests only see rooms that are currently in stock.
 */
public class RoomSearchService {
    private RoomInventory inventory;
    private List<Room> roomTemplates;

    public RoomSearchService(RoomInventory inventory, List<Room> roomTemplates) {
        this.inventory = inventory;
        this.roomTemplates = roomTemplates;
    }

    /**
     * Searches for available rooms.
     * This method is "Pure"—it does not modify any variables or state.
     */
    public void searchAvailableRooms() {
        System.out.println("\n--- Available Rooms for Booking ---");
        boolean found = false;

        for (Room room : roomTemplates) {
            int availableCount = inventory.getAvailability(room.getRoomType());

            // Validation Logic: Only show rooms with stock > 0
            if (availableCount > 0) {
                displayRoomOption(room, availableCount);
                found = true;
            }
        }

        if (!found) {
            System.out.println("Sorry, no rooms are currently available.");
        }
    }

    private void displayRoomOption(Room room, int count) {
        System.out.println("[" + room.getRoomType() + "]");
        System.out.println("  Price per Night: $" + room.getPricePerNight());
        System.out.println("  Beds: " + room.getNumBeds());
        System.out.println("  In Stock: " + count);
        room.displayRoomFeatures();
        System.out.println("----------------------------------");
    }
}

import java.util.Arrays;
import java.util.List;

public class HotelBookingApp {
    public static void main(String[] args) {
        System.out.println("=== Hotel Booking Management System v1.3 ===\n");

        // 1. Initialize Inventory
        RoomInventory inventoryManager = new RoomInventory();
        inventoryManager.addRoomType("Single Room", 5);
        inventoryManager.addRoomType("Double Room", 0); // Out of stock!
        inventoryManager.addRoomType("Suite Room", 2);

        // 2. Create Room Templates (Domain Models)
        List<Room> roomTemplates = Arrays.asList(
                new SingleRoom(),
                new DoubleRoom(),
                new SuiteRoom()
        );

        // 3. Initialize Search Service
        RoomSearchService searchService = new RoomSearchService(inventoryManager, roomTemplates);

        // 4. Guest triggers a search
        System.out.println("Guest is searching for available rooms...");
        searchService.searchAvailableRooms();
    }
}