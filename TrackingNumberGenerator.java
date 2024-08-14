import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TrackingNumberGenerator {

    // ConcurrentHashMap to store generated tracking numbers for uniqueness verification
    private static final ConcurrentHashMap<String, Boolean> generatedNumbers = new ConcurrentHashMap<>();

    /**
     * Generates a unique tracking number using UUID.
     * UUID ensures global uniqueness and is highly scalable.
     * @return Unique tracking number as a String
     */
    public static String generateTrackingNumber() {
        String trackingNumber;

        do {
            // Generate a random UUID
            UUID uuid = UUID.randomUUID();

            // Convert UUID to string and use as the tracking number
            trackingNumber = uuid.toString();

            // Ensure the tracking number is unique by checking if it's already in the map
        } while (Objects.nonNull(generatedNumbers.putIfAbsent(trackingNumber, true)));

        // Return the unique tracking number
        return trackingNumber;
    }

    public static void main(String[] args) throws InterruptedException {
        // Number of concurrent threads for testing high concurrency
        int numberOfThreads = 100;

        // Executor service to manage multiple threads
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

        // Run the tracking number generation in multiple threads to test scalability and concurrency
        for (int i = 0; i < numberOfThreads; i++) {
            executor.submit(() -> {
                String trackingNumber = generateTrackingNumber();
                System.out.println("Generated Tracking Number: " + trackingNumber);
            });
        }

        // Shutdown the executor and wait for tasks to complete
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        // Verifying the uniqueness of tracking numbers
        System.out.println("Total unique tracking numbers generated: " + generatedNumbers.size());
    }
}
