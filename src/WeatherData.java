import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class WeatherData implements Weather{
     private Scanner scanner;
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

    public WeatherData(String pathToWeatherData) {
        String line = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(pathToWeatherData));
            int dayOfYear = 0;
            while ((line = br.readLine()) != null) {
                String[] lineData = line.split(",");

                if (lineData[0].equals("date")) {
                    continue;   // skip header
                }

//              lineData[0] = date
                for (int hour = 1; hour <= 24; hour++) {
                    double maxTemp = Double.parseDouble(lineData[hour * 3 - 2]);
                    double minTemp = Double.parseDouble(lineData[hour * 3 - 1]);
                    temperature[dayOfYear][hour - 1] = (maxTemp + minTemp) * 0.5;
                    precipitation[dayOfYear][hour - 1] = Double.parseDouble(lineData[hour * 3]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public double getPrecipitation(int dayOfYear, int hour) {
        return precipitation[dayOfYear][hour];
    }
    public double getTemperature(int dayOfYear, int hour) {
        return temperature[dayOfYear][hour];
    }
}
