package org.example;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of elevators: ");
        int numElevators = scanner.nextInt();

        System.out.print("Enter the number of floors: ");
        int numFloors = scanner.nextInt();

        Queue<LiftRequest> requestQueue = new LinkedList<>();

        ExecutorService generatorExecutor = Executors.newSingleThreadExecutor();
        generatorExecutor.submit(new RequestGenerator(requestQueue, 10, numFloors));

        List<Lift> lifts = new ArrayList<>();
        for (int i = 0; i < numElevators; i++) {
            int capacity = 10;
            Lift lift = new Lift(i + 1, capacity);
            lifts.add(lift);
        }


        ExecutorService liftExecutor = Executors.newFixedThreadPool(numElevators);
        while (true) {
            synchronized (requestQueue) {
                while (!requestQueue.isEmpty()) {
                    LiftRequest request = requestQueue.poll();
                    Lift bestLift = null;
                    for (Lift lift : lifts) {
                        if (lift.capacity >= request.people) {
                            bestLift = lift;
                            break;
                        }
                    }
                    if (bestLift != null) {
                        bestLift.addRequest(request);
                        liftExecutor.submit(bestLift);
                    }
                }
            }

            boolean allFinished = true;
            for (Lift lift : lifts) {
                if (!lift.isFinished()) {
                    allFinished = false;
                    break;
                }
            }

            if (allFinished) {
                break;
            }

            Thread.sleep(1000);
        }

        liftExecutor.shutdown();
        generatorExecutor.shutdown();

        liftExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        generatorExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        System.out.println("All lifts have finished their work.");
    }
}