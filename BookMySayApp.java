public class AddOnService {
    private String name;
    private double price;

    public AddOnService(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return name + " ($" + price + ")";
    }
}
import java.util.*;

/**
 * Manages the association between Reservations and their Add-On services.
 */
public class ServiceManager {
    // Key: RoomID (from confirmed booking), Value: List of services selected
    private Map<String, List<AddOnService>> bookingServices;

    public ServiceManager() {
        this.bookingServices = new HashMap<>();
    }

    /**
     * Attaches a service to a specific room allocation.
     */
    public void addServiceToBooking(String roomID, AddOnService service) {
        bookingServices.computeIfAbsent(roomID, k -> new ArrayList<>()).add(service);
        System.out.println("Added " + service.getName() + " to Booking " + roomID);
    }

    /**
     * Calculates the total cost of all add-ons for a specific booking.
     */
    public double calculateAddOnTotal(String roomID) {
        List<AddOnService> services = bookingServices.get(roomID);
        if (services == null) return 0.0;

        return services.stream()
                .mapToDouble(AddOnService::getPrice)
                .sum();
    }

    public void displayServicesForBooking(String roomID) {
        List<AddOnService> services = bookingServices.getOrDefault(roomID, new ArrayList<>());
        if (services.isEmpty()) {
            System.out.println("No add-on services for " + roomID);
        } else {
            System.out.println("Services for " + roomID + ": " + services);
        }
    }
}

public class HotelBookingApp {
    public static void main(String[] args) {
        System.out.println("=== Hotel Booking Management System v1.6 ===\n");

        // 1. Setup existing components
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Suite Room", 1);
        BookingRequestQueue queue = new BookingRequestQueue();
        queue.enqueueRequest(new Reservation("Alice", "Suite Room"));

        BookingService bookingService = new BookingService(inventory, queue);
        ServiceManager serviceManager = new ServiceManager();

        // 2. Process Booking to get a Room ID (e.g., S-101)
        bookingService.processAllRequests();
        String aliceRoomID = "S-101"; // Generated in the previous step

        // 3. Guest selects Add-Ons
        AddOnService breakfast = new AddOnService("Buffet Breakfast", 25.0);
        AddOnService spa = new AddOnService("Spa Treatment", 120.0);

        serviceManager.addServiceToBooking(aliceRoomID, breakfast);
        serviceManager.addServiceToBooking(aliceRoomID, spa);

        // 4. Final summary
        serviceManager.displayServicesForBooking(aliceRoomID);
        System.out.println("Total Add-on Cost: $" + serviceManager.calculateAddOnTotal(aliceRoomID));
    }
}