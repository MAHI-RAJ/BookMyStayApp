import java.io.*;
import java.util.*;

// 1. Ensure your core classes are Serializable
class Booking implements Serializable {
    private static final long serialVersionUID = 1L;
    String guestName;
    int roomNumber;
    // Constructor and getters...
}

public class UseCase12DataPersistenceRecovery {
    private static final String STORAGE_FILE = "hotel_state.ser";
    private Map<Integer, Boolean> inventory = new HashMap<>();
    private List<Booking> bookingHistory = new ArrayList<>();

    public static void main(String[] args) {
        UseCase12DataPersistenceRecovery app = new UseCase12DataPersistenceRecovery();

        // STEP 1: Startup Recovery
        app.loadSystemState();

        // Simulate app operations...
        app.processSampleBookings();

        // STEP 2: Shutdown Persistence
        app.saveSystemState();
    }

    /**
     * SERIALIZATION: Writing objects to a file
     */
    public void saveSystemState() {
        System.out.println("System preparing for shutdown. Serializing state...");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(STORAGE_FILE))) {
            oos.writeObject(inventory);
            oos.writeObject(bookingHistory);
            System.out.println("Data successfully saved to " + STORAGE_FILE);
        } catch (IOException e) {
            System.err.println("Error saving state: " + e.getMessage());
        }
    }

    /**
     * DESERIALIZATION: Restoring objects from a file
     */
    @SuppressWarnings("unchecked")
    public void loadSystemState() {
        File file = new File(STORAGE_FILE);

        // FAILURE TOLERANCE: Handle missing files gracefully
        if (!file.exists()) {
            System.out.println("No persistence file found. Initializing fresh system state.");
            initializeDefaultInventory();
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(STORAGE_FILE))) {
            inventory = (Map<Integer, Boolean>) ois.readObject();
            bookingHistory = (List<Booking>) ois.readObject();
            System.out.println("System state recovered successfully. Records loaded.");
        } catch (IOException | ClassNotFoundException e) {
            // FAILURE TOLERANCE: Handle corrupted data
            System.err.println("Critical Error: Persisted data is corrupted. Starting with default state.");
            initializeDefaultInventory();
        }
    }

    private void initializeDefaultInventory() {
        for (int i = 101; i <= 110; i++) inventory.put(i, true);
    }

    private void processSampleBookings() {
        // Logic for adding bookings goes here...
    }
}