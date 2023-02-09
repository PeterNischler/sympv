import java.util.Arrays;
import java.util.Random;

/* GUT: hoher Klassenzusammenhalt. für schwachen Klassenzusammenhalt müsste ich hier Dinge weglassen können.
 * Sobald ich hier etwas weglasse funktioniert der Code aber nicht mehr.
 */

public class House {
    private double latitude;
    private double longitude;
    private final Random random;

    /**
     * meters above sea level
      */
    private int height; // >= 0

    private double totalEnergyConsumption; // >= 0
    /**
     * in kilowatts
     */
    private double actualEnergyConsumption=0; // >= 0
    /**
     * in watts
     */

    private int actualHour = -1; // >= 0

    private PVSystem[] PVSystems;
    /**
     * in watts
     */

    private final double maximumPowerUsage;

    public House(double maximumPowerUsage, double latitude, double longitude, int height, PVSystem[] PVSystems, Random random) {
        this.maximumPowerUsage = maximumPowerUsage;
        this.latitude = latitude;
        this.longitude = longitude;
        this.height = height;
        this.PVSystems = PVSystems;
        this.random = random;
        this.totalEnergyConsumption = 0;
    }

    /**
     * in watts per hour
     * @param hourOfDay
     * @return actual Energy Consumption
     * source for power usage:
     * https://didaktik.physik.fu-berlin.de/projekte/dbu/Module/Modul1_eigener_Stromverbrauch_ohne_Logos_korr_HG1.pdf
     */
    public double currentEnergyConsumption(int hourOfDay){
    /*Vorbedingung:
        0<= hourOfDay <= 24
      Nachbedingung:
        actualEnergyConsumption >= 0   (Verbrauch kann nicht negativ sein)
    */
        double usage = 0;
        if(actualHour != hourOfDay) {

            if (hourOfDay >= 0 && hourOfDay < 6) {
                usage = 0.2 + (random.nextDouble() - 0.5) * 0.1;
            } else if (hourOfDay >= 6 && hourOfDay < 9) {
                usage = 0.65 + (random.nextDouble() - 0.5) * 0.3;
            } else if (hourOfDay >= 9 && hourOfDay < 15) {
                usage = 0.5 + (random.nextDouble() - 0.5) * 0.2;
            } else if (hourOfDay >= 15 && hourOfDay < 18) {
                usage = 0.4 + (random.nextDouble() - 0.5) * 0.15;
            } else if (hourOfDay >= 18 && hourOfDay < 22) {
                usage = 0.8 + (random.nextDouble() - 0.5) * 0.4;
            } else {
                usage = 0.4 + (random.nextDouble() - 0.5) * 0.14;
            }

            actualEnergyConsumption = usage * maximumPowerUsage;
            totalEnergyConsumption += actualEnergyConsumption/1000;
            actualHour = hourOfDay;
        }

       return actualEnergyConsumption;

    }
    public double currentNetEnergy(int hourOfDay, double producedEnergy){
    /*Vorbedingung:
        0<= hourOfDay <= 24
        producedEnergy > 0
      *Nachbedingung:
    */
        return producedEnergy - currentEnergyConsumption(hourOfDay);
    }

    public double getLatitude(){
            return latitude;
    }

    public double getLatitudeRad() {
        return Math.toRadians(latitude);
    }

    public double getHeight() {
        return height;
    }

    public double getTotalEnergyConsumption() {
        return totalEnergyConsumption;
    }

    public PVSystem[] getPVSystems() {
        return PVSystems;
    }

    public String toString() {
        return "House{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", height=" + height +
                ", PVSystems=" + Arrays.toString(PVSystems) +
                '}';
    }
}
