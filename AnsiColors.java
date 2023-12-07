package traffic;

public enum AnsiColors {
    ANSI_RED("\u001B[31m"),
    ANSI_GREEN("\u001B[32m"),
    ANSI_YELLOW("\u001B[33m"),
    ANSI_RESET("\u001B[0m");

    private final String colorName;
    private AnsiColors(String colorName) {
        this.colorName = colorName;
    }
    public String getColorName() {
        return colorName;
    }

    public AnsiColors getAnsiColor(String nameColor) {
        for (AnsiColors ansiColor : AnsiColors.values()) {
            if (ansiColor.getColorName().equals(nameColor))
                return ansiColor;
        }
        return ANSI_RESET;
    }
}
