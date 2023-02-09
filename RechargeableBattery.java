/*GUT: Klassenzusammenhalt ist hier hoch: Würde ich hier eine der Membervariablen weglassen, würde der Code nicht mehr funktionieren*/
public class RechargeableBattery {

    private double storageCapacity; // >= 0
    /**
     * in Ah
     */

    private double actualCapacity; // >= 0
    /**
     * in Ah
     */

    private final double c_Factor;
    /**
     * The C-factor can be used to specify the allowed charging and discharging current
     * https://de.wikipedia.org/wiki/Kapazit%C3%A4t_(galvanische_Zelle)#C-Faktor
     */

    private final double maximumChargingCurrent;
    /**
     * C_Faktor * StorageCapacity (Electric charge) = maximum charging current
     */

    public RechargeableBattery(double totalStorageCapacity, double c_Factor, double initialCapacity ) {
        this.storageCapacity = totalStorageCapacity;
        this.c_Factor = c_Factor;
        this. maximumChargingCurrent =storageCapacity * c_Factor ;
        this.actualCapacity = initialCapacity;
    }

    public RechargeableBattery(double totalStorageCapacity, double c_Factor ) {
        // used for empty Batteries
        this(totalStorageCapacity,c_Factor,0);
    }


    /**
     * in watts
     * @param Power in Watts
     * @param Voltage in Volts
     * @return remaining Power in Watts
     */
    public double charge(double Power, double Voltage){
        /*Vorbedingung:
            Power und Voltage müssen beide > 0 sein
          Nachbedingungen:
            actualCapacity > 0 und  returnValue > 0
        */
        double producedCurrent, remainingCurrent;
        producedCurrent = Power/Voltage;


        if (actualCapacity + producedCurrent > storageCapacity){

            remainingCurrent = actualCapacity + producedCurrent - storageCapacity;
            actualCapacity = storageCapacity;
        }
        else{
            actualCapacity += producedCurrent;
            remainingCurrent = 0;
        }

        return remainingCurrent*Voltage;
    }



    /**
     * in watts
     * @param Power in Watts
     * @param Voltage in Volts
     * @return remaining Power in Watts
     */
    public double discharge(double Power, double Voltage){
        /*Vorbedingung:
            Power und Voltage müssen beide > 0 sein
          Nachbedingungen:
            actualCapacity > 0 und  returnValue > 0
        */
        double  usedCurrent, remainingCurrent;

        usedCurrent = Power/Voltage;

        if (actualCapacity < usedCurrent){
            remainingCurrent =  usedCurrent - actualCapacity; //battery is empty
            actualCapacity = 0;
        }
        else{
            remainingCurrent =  0; // still power in battery
            actualCapacity -= usedCurrent;
        }

        return remainingCurrent*Voltage;

    }

    public String toString() {
        return "RechargeableBattery{" +
                "storageCapacity=" + storageCapacity +
                ", actualCapacity=" + actualCapacity +
                '}';
    }
}
