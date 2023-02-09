public interface Irradiance {

    /*GUT: die Verwendung eines Interfaces ermöglicht es die dahinter liegende Implementierung leicht auszutauschen*/

    /* SCHLECHT: In einem Interface sollte keine Implementierungen von Code stattfinden.
     * Da es hier zwei Methoden gibt welche für beide Implementierungen dieses Interfaces gelten wäre hier die Verwendung einer abstrakten Klasse besser
     */

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
    /*Vorbedingung:
            0<= hourOfDay <= 24
        */

    public double getZenithRad();
    public double getAzimuthRad();
    public double getElevationDeg();

    public double getHorizontalGlobalIrradiance(int dayOfYear, int hour);
    /*Vorbedingung:
        1<=dayOfYear<=365
        0<= hour <= 24
     */
}
