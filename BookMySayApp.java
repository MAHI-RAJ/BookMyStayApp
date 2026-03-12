
public abstract class Room {
    private String roomType;
    private int numBeds;
    private double pricePerNight;

    public Room(String roomType, int numBeds, double pricePerNight) {
        this.roomType = roomType;
        this.numBeds = numBeds;
        this.pricePerNight = pricePerNight;
    }


    public String getRoomType() { return roomType; }
    public int getNumBeds() { return numBeds; }
    public double getPricePerNight() { return pricePerNight; }

    public abstract void displayRoomFeatures();
}
class SingleRoom extends Room {
    public SingleRoom() { super("Single Room", 1, 100.0); }

    @Override
    public void displayRoomFeatures() {
        System.out.println("Features: Compact space, perfect for solo travelers, High-speed Wi-Fi.");
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() { super("Double Room", 2, 180.0); }

    @Override
    public void displayRoomFeatures() {
        System.out.println("Features: Queen size beds, spacious work desk, mini-fridge.");
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() { super("Suite Room", 3, 350.0); }

    @Override
    public void displayRoomFeatures() {
        System.out.println("Features: Master bedroom, private lounge, 24/7 butler service.");
    }
}
public class HotelBookingApp {

    // Static Availability Representation (Limitation: Hardcoded state)
    private static int singleRoomAvailability = 5;
    private static int doubleRoomAvailability = 3;
    private static int suiteRoomAvailability = 2;

    public static void main(String[] args) {
        System.out.println("=== Hotel Booking Management System v1.1 ===\n");

        // Polmorphism: Using the base 'Room' reference for different objects
        Room single = new SingleRoom();
        Room dbl = new DoubleRoom();
        Room suite = new SuiteRoom();

        displayInventory(single, singleRoomAvailability);
        displayInventory(dbl, doubleRoomAvailability);
        displayInventory(suite, suiteRoomAvailability);
    }

    public static void displayInventory(Room room, int count) {
        System.out.println("Room Type: " + room.getRoomType());
        System.out.println("Beds:      " + room.getNumBeds());
        System.out.println("Price:     $" + room.getPricePerNight());
        System.out.println("Available: " + count);
        room.displayRoomFeatures();
        System.out.println("----------------------------------------------");
    }
}