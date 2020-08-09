/*
    A benchmark for propositional machines in BeepBeep 3
    Copyright (C) 2020 Laboratoire d'informatique formelle

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
    picker.reset();
    float[] readings = new float[num_readings];
    for (int i = 0; i < num_readings; i++)
    {
      readings[i] = picker.pick();
    }
    return readings;
  }

}
