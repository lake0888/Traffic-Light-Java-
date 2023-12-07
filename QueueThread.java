package traffic;

import java.io.IOException;
import java.util.Scanner;

class QueueThread extends Thread {
    private int seconds;
    private boolean isOn;

    private boolean isVisible;

    private int maxRoads;
    private int intervals;

    private Road[] roads;
    private int front;
    private int rear;
    private int elements;

    Scanner scanner = new Scanner(System.in);

    public QueueThread(int maxRoads, int intervals) {
        this.seconds = 0;
        this.isOn = true;
        this.isVisible = false;

        this.maxRoads = maxRoads;
        this.intervals = intervals;
        this.roads = new Road[maxRoads];

        this.front = 0;
        this.rear = -1;
        this.elements = 0;
    }

    @Override
    public void run() {
        while(this.isOn) {
            try {

                if (isVisible) {
                    showCurrentState();
                }

                sleep(1_000L);
                seconds++;

                if (isVisible) {
                    clearOutput();
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void addRoads() throws InterruptedException {
        System.out.print("Input road name: ");
        String roadName = scanner.next();
        if (this.elements == this.maxRoads) {
            System.out.println("Queue is full");
        } else {
            int next = (this.rear < 0) ? 0 : (this.rear + 1) % this.maxRoads;

            Road road = new Road(roadName);
            this.rear = next;
            this.elements++;
            this.roads[next] = road;
            //CALCULATE INTERVAL
            calculateInterval(road);
            updateMaxSeconds();
            System.out.printf("%s added!", roadName);
        }
    }

    public void deleteRoads() throws InterruptedException {
        if (this.elements == 0) {
            System.out.println("Queue is empty");
        } else {
            Road road = this.roads[front];
            System.out.printf("%s deleted!", road.getNameRoad());
            this.roads[front] = null;

            this.front = (this.front + 1) % this.maxRoads;
            this.elements--;
            updateMaxSeconds();
        }
    }

    private void showCurrentState() throws InterruptedException {
        System.out.printf("! %ds. have passed since system startup !%n", this.seconds);
        System.out.printf("! Number of roads: %d !%n", this.maxRoads);
        System.out.printf("! Interval: %d !%n", this.intervals);

        for (int i = this.front; i <= this.rear; i++) {
            if (this.roads[i] != null) {
                System.out.printf("%n%s", this.roads[i]);
                updateRoad(roads[i]);
            }
        }

        System.out.println("\n! Press \"Enter\" to open menu !");
    }

    private void updateRoad(Road road) {
        int seconds = road.getSeconds() - 1;
        road.setSeconds(seconds);
        if (seconds == 0) {
            if (elements == 1) {
                if (!road.isOpen()) road.setOpen(true);
            } else {
                road.setOpen(!road.isOpen());
            }
            road.updateAnsiColor();
            road.setSeconds(road.isOpen() ? this.intervals : Road.maxSeconds);
        }
    }

    public void clearOutput() throws InterruptedException {
        try {
            var clearCommand = System.getProperty("os.name").contains("Windows")
                    ? new ProcessBuilder("cmd", "/c", "cls")
                    : new ProcessBuilder("clear");
            clearCommand.inheritIO().start().waitFor();
        }
        catch (IOException | InterruptedException e) {}
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        this.isOn = on;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public int getMaxRoads() {
        return maxRoads;
    }

    public void setMaxRoads(int maxRoads) {
        this.maxRoads = maxRoads;
    }

    public Road[] getRoads() {
        return roads;
    }

    public void setRoads(Road[] roads) {
        this.roads = roads;
    }

    public int getIntervals() {
        return intervals;
    }

    public void setIntervals(int intervals) {
        this.intervals = intervals;
    }

    /*
    private void calculateInterval() throws InterruptedException {
        for (int i = front; i <= rear; i++) {
            if (roads[i] != null) {
                if (i == front) {
                    roads[front].setOpen(true);
                    roads[front].setSeconds(this.intervals);
                    roads[front].setAnsiColor(AnsiColors.ANSI_GREEN);
                } else {
                    roads[i].setOpen(false);
                    if (i == front + 1) {
                        roads[i].setSeconds(this.intervals);
                    } else {
                        roads[i].setSeconds(roads[i - 1].getSeconds() + this.intervals);
                    }
                    roads[i].setAnsiColor(AnsiColors.ANSI_RED);
                }
            }
        }

        if (roads[rear] != null) Road.maxSeconds = roads[rear].getSeconds();
    }
     */

    private void calculateInterval(Road road) throws InterruptedException {
        if (roads[rear] != null) {
            if (elements == 1) {
                road.setSeconds(this.intervals);
                road.setOpen(true);
            } else if (elements == 2) {
                road.setSeconds(roads[rear - 1].getSeconds());
                road.setOpen(!roads[rear - 1].isOpen());
            } else {
                road.setSeconds(roads[rear - 1].getSeconds() + this.intervals);
                road.setOpen(false);
            }
        }
    }

    private void updateMaxSeconds() {
        Road.maxSeconds = (elements < 3) ? this.intervals : (elements - 1) * this.intervals;
    }

}