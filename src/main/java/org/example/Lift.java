package org.example;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Lift implements Runnable {
    int liftId;
    int currentFloor = 0;
    int capacity;
    List<LiftRequest> requests = new LinkedList<>();
    private final Queue<LiftRequest> pendingRequests = new LinkedList<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(1);
    private final AtomicBoolean isFinished = new AtomicBoolean(false);

    public Lift(int liftId, int capacity) {
        this.liftId = liftId;
        this.capacity = capacity;
    }

    public synchronized void addRequest(LiftRequest request) {
        pendingRequests.add(request);
    }

    private void processRequest(LiftRequest request) {
        System.out.println("Lift " + liftId + " is processing: " + request);
        int peopleToPickUp = Math.min(request.people, capacity);
        capacity -= peopleToPickUp;
        System.out.println("Lift " + liftId + " picks up " + peopleToPickUp + " people from floor " + request.fromFloor);
        try {
            Thread.sleep(Math.abs(currentFloor - request.fromFloor) * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        currentFloor = request.fromFloor;
        System.out.println("Lift " + liftId + " arrives at floor " + request.fromFloor);
        capacity += peopleToPickUp;

        try {
            Thread.sleep(Math.abs(request.toFloor - currentFloor) * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        currentFloor = request.toFloor;
        System.out.println("Lift " + liftId + " arrives at floor " + request.toFloor);
    }

    @Override
    public void run() {
        while (!pendingRequests.isEmpty()) {
            LiftRequest request = pendingRequests.poll();
            processRequest(request);
        }
        isFinished.set(true);
        System.out.println("Lift " + liftId + " finished all requests.");
    }

    public boolean isFinished() {
        return isFinished.get();
    }
}