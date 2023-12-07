package traffic;

import java.util.Objects;

public class Road {
    private String nameRoad;
    private boolean isOpen;
    private AnsiColors ansiColor;
    public static int maxSeconds;

    private int seconds;

    public Road(String nameRoad) {
        this.nameRoad = nameRoad;
        this.isOpen = false;
        this.ansiColor = AnsiColors.ANSI_RESET;
        this.seconds = 0;
    }

    public void updateAnsiColor() {
        ansiColor = (this.isOpen) ? AnsiColors.ANSI_GREEN : AnsiColors.ANSI_RED;
    }

    public String getNameRoad() {
        return nameRoad;
    }

    public void setNameRoad(String nameRoad) {
        this.nameRoad = nameRoad;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public AnsiColors getAnsiColor() {
        return ansiColor;
    }

    public void setAnsiColor(AnsiColors ansiColor) {
        this.ansiColor = ansiColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Road road = (Road) o;
        return isOpen == road.isOpen && Objects.equals(nameRoad, road.nameRoad);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameRoad, isOpen);
    }

    @Override
    public String toString() {
        return String.format("%sRoad \"%s\" will be %s for %ds.%s", ansiColor.getColorName(), nameRoad, (isOpen) ? "open" : "closed", seconds, "\u001B[0m");
    }

    public int getSeconds() {
        return this.seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}
