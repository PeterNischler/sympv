import java.util.Random;

public class WeatherRandom implements Weather{
    private final Random random;
    /**
     * in mm
     * precipitation[dayOfYear][hour] = x;
     */
    private double[][] precipitation = new double[365][24];
    /**
     * in °Celsius
     * temperature[dayOfYear][hour] = x;
     */
    private double[][] temperature = new double[365][24];

    /**
     *
     * @param random
     * @param averageTemp   in °Celsius
     * @param averageBadWeatherDays days when sky is not clear
     * @param avgPrecipitation  in mm
     */
    public WeatherRandom(Random random, double averageTemp, double averageBadWeatherDays, double avgPrecipitation) {
    /*
     Vorbedingung:
           0°C< averageTemp < 40°C
           0<= averageBadWeatherDays >= 365
      Nachbedingung:
        temperature soll von Tag zu Tag verschieden sein
    */
        this.random = random;

        double monthModifier;
        double hourModifier;
        double badWeatherModifier = 1;

        for (int day = 0; day < 365; day++) {
            if (day < 30){            // january
                monthModifier = 0.2;
            } else if (day < 60){     // february
                monthModifier = 0.4;
            } else if (day < 90){     // march
                monthModifier = 0.8;
            } else if (day < 120){    // april
                monthModifier = 1;
            } else if (day < 150){    // may
                monthModifier = 1.2;
            } else if (day < 180){    // june
                monthModifier = 1.4;
            } else if (day < 210){    // july
                monthModifier = 1.6;
            } else if (day < 240){    // august
                monthModifier = 1.2;
            } else if (day < 270){    // september
                monthModifier = 1;
            } else if (day < 300){    // october
                monthModifier = 0.7;
            } else if (day < 330){    // november
                monthModifier = 0.5;
            } else {                        // December
                monthModifier = 0.2;
            }

            boolean weatherIsBad = random.nextDouble() <= averageBadWeatherDays/365;
            if (weatherIsBad) {
                // how bad is the weather?
                badWeatherModifier = 0.1 + 0.9 * random.nextDouble();
            }

            for (int hour = 0; hour < 24; hour++) {
                if (hour < 4) {
                    hourModifier = 0.2;
                } else if (hour < 8) {
                    hourModifier = 0.6;
                } else if (hour < 12) {
                    hourModifier = 1;
                } else if (hour < 16) {
                    hourModifier = 1.5;
                } else if (hour < 20) {
                    hourModifier = 1;
                } else {
                    hourModifier = 0.5;
                }

                /*ERROR: die temperature Werte sind gleich für aufeinanderfolgende Tag, nur bei Monatswechsel ändert sich etwas. */

                temperature[day][hour] = averageTemp * monthModifier * hourModifier * badWeatherModifier;
                if (weatherIsBad) {
                    precipitation[day][hour] = avgPrecipitation * monthModifier * badWeatherModifier;
                } else {
                    precipitation[day][hour] = 0;
                }
            }
        }
    }
    public double getPrecipitation(int dayOfYear, int hour) {
        return precipitation[dayOfYear][hour];
    }

    public double getTemperature(int dayOfYear, int hour) {
        return temperature[dayOfYear][hour];
    }
}
