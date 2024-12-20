package org.example;

class LiftRequest {
    int fromFloor;
    int toFloor;
    int people;

    public LiftRequest(int fromFloor, int toFloor, int people) {
        this.fromFloor = fromFloor;
        this.toFloor = toFloor;
        this.people = people;
    }

    @Override
    public String toString() {
        return "Request from floor " + fromFloor + " to floor " + toFloor + " with " + people + " people.";
    }
}