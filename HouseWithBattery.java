import java.util.Arrays;
import java.util.Random;
/* GUT: hoher Klassenzusammenhalt. für schwachen Klassenzusammenhalt müsste ich hier Dinge weglassen können.
 */

public class HouseWithBattery extends House{
    private RechargeableBattery battery;
    /**
     * in watts
     */

    public HouseWithBattery(double maximumPowerUsage, double latitude, double longitude, int height, PVSystem[] PVSystems, RechargeableBattery battery , Random random) {
        super(maximumPowerUsage, latitude, longitude, height, PVSystems, random);
        this.battery = battery;
    }


    /**
     * in watts
     * @param hourOfDay
     * @param producedEnergy in Watts
     */
    @Override public double currentNetEnergy(int hourOfDay, double producedEnergy){
     /*Vorbedingung:
        0<= hourOfDay <= 24
        producedEnergy > 0
      *Nachbedingung:
    */
        return battery.charge(producedEnergy,230) - battery.discharge(super.currentEnergyConsumption(hourOfDay),230);
    }

    @Override public String toString() {
        return "HouseWithBattery" +
                super.toString().substring(super.toString().indexOf("{"),super.toString().lastIndexOf("}")) +
                ","  + battery.toString() +
                '}';
    }
}
