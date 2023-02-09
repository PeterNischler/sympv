public class IrradianceEstimate implements Irradiance{
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
    /**
     * average of solar energy in Wh/m^2 at atmospheric level
      */
    private double solarConstant;
    /**
     * direct irradiance to the normal surface, => sun irradiance is vertical to PVSystem surface
     */
    private double normalDirect;
    /**
     * diffused horizontal irradiance from the sky
     */
    private double horizontalSky;

    /**
     * normalDirect + horizontalSky
     */
    private double horizontalGlobal;

    /**
     * Turbidity factor according to Linke, absorption and scattering of the solar radiation in dry and clean atmosphere
     */
    private double turbidityFactor;
    private double PVSystem_Tilt_rad;
    private double PVSystem_Azimuth_rad;
    private double height;
    /**
     * laditude of PVsystem in radians
     */
    private double latitude;
    public IrradianceEstimate(House house){
        this.latitude = house.getLatitudeRad();
        this.height = house.getHeight();
        this.PVSystem_Tilt_rad = house.getPVSystems()[0].getTiltRad();
        this.PVSystem_Azimuth_rad = house.getPVSystems()[0].getAzimuthRad();
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
     * turbidity factor according to Linke
     * @param dayOfYear
     * @return
     */
    private static double turbidityFactor(int dayOfYear) {
        if (dayOfYear <= 31) return 3.7;        // January
        if (dayOfYear <= 59) return 4.1;        // February
        if (dayOfYear <= 90) return 4.6;        // March
        if (dayOfYear <= 120) return 5.1;       // April
        if (dayOfYear <= 151) return 5.3;       // May
        if (dayOfYear <= 181) return 6.1;       // June
        if (dayOfYear <= 212) return 6.1;       // July
        if (dayOfYear <= 243) return 5.9;       // August
        if (dayOfYear <= 273) return 5.4;       // September23
        if (dayOfYear <= 304) return 4.2;       // October
        if (dayOfYear <= 334) return 3.6;       // November
        return 3.5;                             // December
    }


    /**
     * Ratio between the diffused irradiance on an inclined surface and a horizontal surface.
     * @return
     */
    public double R_sky(){
        // Calculation the R-Value according to GUSEV for a vertical PVSystem for  diffuse irradiation from clear sky
        double[][] GUSEV = new double[][]{
                {176,   170,    135,    101,    74,    53,     38},
                {171,   166,    132,    99,     73,    53,     38},
                {158,   153,    122,    92,     69,    51,     38},
                {138,   132,    106,    82,     63,    49,     38},
                {114,   108,    88,     70,     56,    46,     38},
                {89,    82,     68,     57,     49,    43,     38},
                {68,    60,     52,     46,     42,    40,     38},
                {55,    47,     41,     38,     37,    37,     38},
                {49,    40,     35,     33,     33,    35,     38},
                {47,    37,     32,     30,     30,    33,     38},
                {46,    36,     31,     28,     29,    32,     38},
                {46,    36,     30,     28,     28,    31,     38},
                {46,    36,     30,     27,     28,    31,     38}
        };

        int I1 = (int) Math.floor((elevationDeg) / 15);
        int I2 = I1 + 1;
        if (I2 > 6) {
            I1 = I2 = 6;
        }

        double Elev_diff = elevationDeg - I1 * 15;

        int J1 = (int) Math.floor(Math.abs((Math.toDegrees((azimuthRad - PVSystem_Azimuth_rad)) / 15)));
        int  J2 = J1 + 1;
        if (J2 > 12) {
            J1 = J2 = 12;
        }

        double  Alf_diff = Math.abs(Math.toDegrees(azimuthRad - PVSystem_Azimuth_rad)) - J1 * 15;

        double R11 = GUSEV[J1][I1];  // [AziDiff1] [Elev1]
        double R12 = GUSEV[J1][I2];  // [AziDiff1] [Elev2]
        double R21 = GUSEV[J2][I1];  // [AziDiff2] [Elev1]
        double R22 = GUSEV[J2][I2];  // [AziDiff2] [Elev2]

        double R110 = (R11 - R12) / 15;  // [AziDiff1] [Elev1] - [AziDiff1] [Elev2]
        double R220 = (R21 - R22) / 15;  // [AziDiff2] [Elev1] - [AziDiff1] [Elev2]

        double R01 = R11 - R110 * Elev_diff;
        double R02 = R21 - R220 * Elev_diff;

        double R0012 = (R02 - R01) / 15;

        return (R01 + R0012 * Alf_diff) / 100;

    }

    /**
     * cacluation of the angle of incidence of solar radiation on an inclined surface (PV system). input in radiants
     * @return
     */
    public double incidenceAngle_rad(){
        // Incidence Angle IA , angle between direction of sun radiation and PVSystem normal in Radiant
        double IA_rad = Math.cos(zenithRad) * Math.cos(PVSystem_Tilt_rad);
        IA_rad = IA_rad + Math.sin(PVSystem_Tilt_rad) * Math.sin(zenithRad) * Math.cos(
                Math.abs(azimuthRad - PVSystem_Azimuth_rad));
        if (IA_rad > 1){
            IA_rad = 1;
        }
        if (IA_rad < 0){
            IA_rad = 0;
        }
        return Math.acos(IA_rad);  // because IA is cos(IA) = cos(zenith)*cos (Surf TA) + ....
    }
    public double getPVSystem_sky() {
        // Sky irradiance
        return horizontalSky * this.R_sky();
    }

    public double getPVSystem_reflec(){
        double Reflection_ground = 0.2;
        return horizontalGlobal * 0.5 * Reflection_ground * (1 - Math.cos(PVSystem_Tilt_rad));
    }
    public double getPVSystem_direct(){
        // Direct irradiance on PVSystem
        return normalDirect * Math.cos(this.incidenceAngle_rad());
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
        double declinationRad = declinationAngle(dayOfYear);
        double hourAngleRad = hourAngleRad(hour);
        turbidityFactor = turbidityFactor(dayOfYear);
        solarConstant = 1367.7 * (1 + 0.033 * Math.cos(Math.PI * 2 * dayOfYear / 365));

        // zenith
        double cosZenithRad = Math.sin(latitude) * Math.sin(declinationRad) + Math.cos(latitude) * Math.cos(declinationRad) * Math.cos(hourAngleRad);
        this.zenithRad = Math.acos(cosZenithRad);
        this.elevationDeg = 90 - Math.toDegrees(this.zenithRad);

        // azimuth
        double Cos_azimuthRad = ((Math.cos(zenithRad)*Math.sin(latitude))
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

        // The penetrated air mass
        double airMass = 1/(0.9 + 9.4 * Math.sin(Math.toRadians(elevationDeg)));

        // normal direct irradiation
        if (Math.toDegrees(zenithRad) < 90) {
            double TaM = 1.294 + 2.4417 * Math.pow(10, -2) * elevationDeg - 3.973 * Math.pow(10, -4) * Math.pow(elevationDeg, 2);
            TaM = TaM + 3.8034 * Math.pow(10, -6) * Math.pow(elevationDeg, 3) - 2.2145 * Math.pow(10, -8) * Math.pow(elevationDeg, 4);
            TaM = (TaM + 5.8832 * Math.pow(10, -11) * Math.pow(elevationDeg, 5)) *(0.506 - 1.0788 * Math.pow(10, -2) * turbidityFactor);

            // Direct normal irradiance (Bestrahlungsstaerke auf Flächennormale, gerichtet zur Sonne)
            this.normalDirect = solarConstant * Math.exp(-turbidityFactor * airMass * Math.exp(-height/8000));

            // Direct horizontal irradiance (Horizontale Bestrahlungsstärke durch Sonne)
            double Horizontal_direct = this.normalDirect * Math.cos(zenithRad);

            // Horizontal irradiance from sky (Horizontale Bestrahlungsstaerke durch den klaren Himmel)
            this.horizontalSky = 0.5 * solarConstant * (Math.sin(Math.toRadians(elevationDeg)))  * (TaM - Math.exp(-turbidityFactor * airMass *  Math.exp(-height/8000)));

            // Global horizontal irradiance (Horizontale Globalbestrahlungsstaerke)
            this.horizontalGlobal = Horizontal_direct + this.horizontalSky;


            // Diffuse irradiance
            double PVSystem_diffuse = getPVSystem_sky() + getPVSystem_reflec();
            // global irradiance
            return getPVSystem_direct() + PVSystem_diffuse;
            // TODO add modifier for weather
        } else {
            return 0;
        }
    }

}

