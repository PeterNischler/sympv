import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class IrradianceData implements Irradiance{
    /**
     * solar irradiance that hits a horizontal surface in watt per m^2
     */
    private double[][] horizontalGlobalIrradiance= new double[365][24];
    private double PVSystem_Tilt_rad;
    private double PVSystem_Azimuth_rad;
    private double height;
    public IrradianceData(House house, String pathToIrradianceData) {
        this.latitude = house.getLatitudeRad();
        this.height = house.getHeight();
        this.PVSystem_Tilt_rad = house.getPVSystems()[0].getTiltRad();
        this.PVSystem_Azimuth_rad = house.getPVSystems()[0].getAzimuthRad();

        String line;
        try {
            BufferedReader br = new BufferedReader(new FileReader(pathToIrradianceData));
            int dayOfYear = 0;
            int hour = 0;
            boolean skipFirstline = true;
            while ((line = br.readLine()) != null) {
                String[] lineData = line.split(",");

                if (skipFirstline) {
                    skipFirstline = false;
                    continue;
                }

                horizontalGlobalIrradiance[dayOfYear][hour] = Double.parseDouble(lineData[2]);

                if (hour == 23){
                    hour = 0;
                    dayOfYear++;
                } else {
                    hour++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * laditude of pv system on the ground in radians
     */
    private double latitude;
    /**
     * solar elevation in degrees
     */
    private double elevationDeg;
    /**
     * solar zenith in radians
     */
    private double zenithRad;
    /**
     * solar azimuth in radians
     *  0° => N, 90° => E, 180° => S, 270° => W
     */
    private double azimuthRad;

    public void calculateSolarPosition(int dayOfYear, int hourOfDay){
        double declinationRad = declinationAngle(dayOfYear);
        double hourAngleRad = hourAngleRad(hourOfDay);

        // zenith
        double cosZenithRad = Math.sin(latitude) * Math.sin(declinationRad) + Math.cos(latitude) * Math.cos(declinationRad) * Math.cos(hourAngleRad);
        zenithRad = Math.acos(cosZenithRad);
        elevationDeg = 90 - Math.toDegrees(this.zenithRad);

        // azimuth
        double Cos_azimuthRad = ((Math.cos(zenithRad) * Math.sin(latitude))
                - Math.sin(declinationRad)) / (Math.cos(latitude)*Math.sin(zenithRad));

        double Azimuth_deg = 0;
        if (hourAngleRad == 0) {
            Azimuth_deg = 180;
        }
        if (hourAngleRad > 0) {
            Azimuth_deg = 180 + Math.toDegrees(Math.acos(Cos_azimuthRad));
        }
        if (hourAngleRad < 0) {
            Azimuth_deg = 180 - Math.toDegrees(Math.acos(Cos_azimuthRad));
        }
        this.azimuthRad = Math.toRadians(Azimuth_deg);
    }

    /**
     * ...
     * 10:00 => -30°
     * 11:00 => -15°
     * 12:00 => 0°
     * 13:00 => 15°
     * 14:00 => 30°
     * ...
     * @param hourOfDay
     * @return radiant
     */
    private double hourAngleRad(int hourOfDay){
        return Math.toRadians(15.0) * (hourOfDay - 12.0);
    }

    /**
     * solar declination angle
     * max 23.44°, min -23.44°
     * @return radiant
     */
    private double declinationAngle(int dayOfYear){
        double fy_rad = (2 * Math.PI / 365) * (dayOfYear - 1);
        // Declination according to Spancer, Reno, Hansen, Stein  equation 5 in Radiant
        return 0.006918
                - 0.399912 * Math.cos(fy_rad)
                + 0.070257 * Math.sin(fy_rad)
                - 0.006758 * Math.cos(2 * fy_rad)
                + 0.000907 * Math.sin(2 * fy_rad)
                - 0.002697 * Math.cos(3 * fy_rad)
                + 0.00148 * Math.sin(3 * fy_rad);
    }

    public double getZenithRad(){
        return this.zenithRad;
    }
    public double getAzimuthRad(){
        return this.azimuthRad;
    }
    public double getElevationDeg(){
        return this.elevationDeg;
    }
    public double getHorizontalGlobalIrradiance(int dayOfYear, int hour){
        this.calculateSolarPosition(dayOfYear, hour);
        // TODO incidence angle diffused light
        return horizontalGlobalIrradiance[dayOfYear-1][hour];
    }

}
