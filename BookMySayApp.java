
/**
 * Represents a guest's intent to book a room.
 * This is a "Data Carrier" object used in the intake process.
 */
public class Reservation {
    private String guestName;
    private String requestedRoomType;

    public Reservation(String guestName, String requestedRoomType) {
        this.guestName = guestName;
        this.requestedRoomType = requestedRoomType;
    }

    public String getGuestName() { return guestName; }
    public String getRequestedRoomType() { return requestedRoomType; }

    @Override
    public String toString() {
        return "Request [Guest: " + guestName + ", Room: " + requestedRoomType + "]";
    }
}

import java.util.LinkedList;
import java.util.Queue;

/**
 * Manages incoming booking requests using a FIFO Queue.
 * Ensures fairness and order preservation.
 */
public class BookingRequestQueue {
    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        // LinkedList is a standard implementation of the Queue interface
        this.requestQueue = new LinkedList<>();
    }

    /**
     * Adds a new booking request to the end of the line.
     */
    public void enqueueRequest(Reservation reservation) {
        requestQueue.add(reservation);
        System.out.println("Enqueued: " + reservation.getGuestName() + " for " + reservation.getRequestedRoomType());
    }

    /**
     * Views the next request in line without removing it.
     */
    public Reservation peekNextRequest() {
        return requestQueue.peek();
    }

    /**
     * Removes and returns the first request in line for processing.
     */
    public Reservation dequeueNextRequest() {
        return requestQueue.poll();
    }

    public void displayQueueStatus() {
        System.out.println("\n--- Current Booking Queue ---");
        if (requestQueue.isEmpty()) {
            System.out.println("No pending requests.");
        } else {
            requestQueue.forEach(System.out::println);
        }
        System.out.println("------------------------------");
    }

    public boolean isEmpty() {
        return requestQueue.isEmpty();
    }
}

public class HotelBookingApp {
    public static void main(String[] args) {
        System.out.println("=== Hotel Booking Management System v1.4 ===\n");

        // Initialize our Intake Queue
        BookingRequestQueue intake = new BookingRequestQueue();

        // Simulate guests arriving at the desk in a specific order
        intake.enqueueRequest(new Reservation("Alice", "Suite Room"));
        intake.enqueueRequest(new Reservation("Bob", "Single Room"));
        intake.enqueueRequest(new Reservation("Charlie", "Suite Room"));

        // Display the line (Arrival order: Alice -> Bob -> Charlie)
        intake.displayQueueStatus();

        // Preview the person at the front of the line
        System.out.println("Next to be processed: " + intake.peekNextRequest());
    }
}