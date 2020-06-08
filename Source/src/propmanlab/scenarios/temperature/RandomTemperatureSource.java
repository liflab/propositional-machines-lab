package propmanlab.scenarios.temperature;

import ca.uqac.lif.synthia.Picker;
import propmanlab.MyLaboratory;

public class RandomTemperatureSource extends SimulatedTemperatureSource
{

  public RandomTemperatureSource(float min_temp, float max_temp, int interval, Picker<Float> picker)
  {
    super(min_temp, max_temp, interval, getReadings(MyLaboratory.MAX_TRACE_LENGTH, picker));
  }
  
  public static float[] getReadings(int num_readings, Picker<Float> picker)
  {
    float[] readings = new float[num_readings];
    for (int i = 0; i < num_readings; i++)
    {
      readings[i] = picker.pick();
    }
    return readings;
  }

}
