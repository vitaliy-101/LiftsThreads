package org.example;

import java.util.Queue;
import java.util.Random;

class RequestGenerator implements Runnable {
    private final Queue<LiftRequest> requestQueue;
    private final Random random = new Random();
    private final int maxRequests;
    private final int numFloors;

    public RequestGenerator(Queue<LiftRequest> requestQueue, int maxRequests, int numFloors) {
        this.requestQueue = requestQueue;
        this.maxRequests = maxRequests;
        this.numFloors = numFloors;
    }

    @Override
    public void run() {
        for (int i = 0; i < maxRequests; i++) {
            int fromFloor = random.nextInt(numFloors);
            int toFloor = random.nextInt(numFloors);
            while (toFloor == fromFloor) {
                toFloor = random.nextInt(numFloors);
            }
            int people = random.nextInt(5) + 1;
            LiftRequest request = new LiftRequest(fromFloor, toFloor, people);
            requestQueue.add(request);
            System.out.println("Generated new request: " + request);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
