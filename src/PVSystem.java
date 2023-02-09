public class PVSystem {
    /**
     * some identifier like "main roof", or "garage roof"...
     */
    private String name;

    /**
     *   degrees 0° => horizontal, 90° => vertical
     */
    private double tilt; // 0° <= tilt <= 90°
    /**
     * meters squared
     */
    private double area;
    /**
     *  0° => N, 90° => E, 180° => S, 270° => W
     */
    private double azimuth;
    /**
     * percentage of the energy that hits the PV panels and can be converted to power
     */
    private double efficiency; // 0.0 <= efficiency <= 1.0
    /**
     * temperature of the PV panels
     */
    private double temperature;
    /**
     * nominal operating cell temperature (°C) at these conditions:
     * Irradiance on cell surface = 800 W/m2
     * Air Temperature = 20°C
     * Wind Velocity = 1 m/s
     * source: https://www.pveducation.org/pvcdrom/modules-and-arrays/nominal-operating-cell-temperature
     */
    private double NOCT;

    /**
     * percentage loss per degree Celsius, usualle between 0.3% and 0.5%
     */
    private double lossPerDegreeC = 0.4;
    /**
     * calculate power loss through temperature
     */
    private double powerLossTemperature;


    /**
     * stores max Energy produced in kWp
     */
    private double kWp = 0.0;// >= 0

    /**
     * stores max maxAirTemperature
     */
    private double maxAirTemperature=-273.0;
    private double minAirTemperature=10000.0;

    public PVSystem(String name, double area, double efficiency, double tilt, double azimuth, double NOCT) {
        this.name = name;
        this.area = area;
        this.efficiency = efficiency;
        this.tilt = tilt;
        this.azimuth = azimuth;
        this.NOCT = NOCT;
    }

    /**
     * watts
     * @param irradiance to PV surface in watts
     * @return
     * Source https://www.pveducation.org/pvcdrom/modules-and-arrays/nominal-operating-cell-temperature
     * https://hjtpv.com/temperature-coefficient-in-photovoltaic/
     */
    public double currentEnergyProduction(double irradiance, double airTemperature) {
    /*Vorbedingungen:
        irradiance > 0
      Nachbedingungen:
        WProduced (returnValue) >= 0
    */
        double WProduced;

        if (airTemperature > maxAirTemperature){
            maxAirTemperature = airTemperature;
        }

        if (airTemperature < minAirTemperature){
            minAirTemperature = airTemperature;
        }
        //temp of PVSystem
        temperature = airTemperature + (NOCT - 20)/80 * irradiance/100;  // kW/m^2 to mW/cm^2
        powerLossTemperature = (temperature - 25) * lossPerDegreeC;

        WProduced = irradiance * area * efficiency * (1 - powerLossTemperature/100);

        if (WProduced/1000 > kWp){
            kWp = WProduced/1000;
        }
        return WProduced;
    }

    /**
     *
     * @return max produced Power of the PV system in kW
     */
    public double getKWp() {
        return kWp;
    }

    public double getTiltRad() {
        return Math.toRadians(tilt);
    }

    public double getAzimuthRad() {
        return Math.toRadians(azimuth);
    }

    public double getMaxAirTemperature() {
        return maxAirTemperature;
    }
    public double getMinAirTemperature() {
        return minAirTemperature;
    }

    public String toString() {
        return  "PVSystem{" +
                "tilt=" + tilt +
                ", area=" + area +
                ", azimuth=" + azimuth +
                ", efficiency=" + efficiency +
                '}';
    }
}