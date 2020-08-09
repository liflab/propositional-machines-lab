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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ca.uqac.lif.cep.ltl.Troolean;
import ca.uqac.lif.cep.propman.ConcreteMultiEvent;
import ca.uqac.lif.cep.propman.MultiEvent;
import ca.uqac.lif.cep.propman.Valuation;
import ca.uqac.lif.cep.propman.ValuationIterator;
import propmanlab.source.MultiEventFileSource;

public class TemperatureSource extends MultiEventFileSource
{
  /**
   * The name of the file to read from
   */
  protected static final transient String FILENAME = "data/temperature/machine_temperature_system_failure.csv";
  
  /**
   * The minimum temperature found in the input file
   */
  public static final float s_minTemp = 70;
  
  /**
   * The maximum temperature found in the input file
   */
  public static final float s_maxTemp = 95;
  
  /**
   * The interval of temperatures
   */
  public static final float s_interval = 1;
    
  public TemperatureSource()
  {
    super(FILENAME);
  }
  
  public TemperatureSource(int num_events)
  {
    super(FILENAME, num_events);
  }
  
  @Override
  protected MultiEvent getEvent()
  {
    String line = m_scanner.nextLine();
    while (line.startsWith("timestamp")) // Ignore 1st line
    {
      line = m_scanner.nextLine();
    }
    String[] parts = line.split(",");
    double temp = Double.parseDouble(parts[1]);
    return getEventFromTemperature(temp);
  }
  
  /**
   * Gets a multi-event from a temperature reading
   * @param temp The temperature
   * @return The multi-event encoding this temperature reading
   */
  public static MultiEvent getEventFromTemperature(double temp, double min_temp, double max_temp, double interval)
  {
    int true_variable = getInterval(temp, min_temp, interval);
    Valuation v = new Valuation();
    for (int i = 0; i < Math.ceil((max_temp - min_temp) / interval); i++)
    {
      if (i == true_variable)
      {
        v.put("t" + i, Troolean.Value.TRUE);
      }
      else
      {
        v.put("t" + i, Troolean.Value.FALSE);
      }
    }
    return new ConcreteMultiEvent(v);
  }
  
  /**
   * Gets a multi-event from a temperature reading
   * @param temp The temperature
   * @return The multi-event encoding this temperature reading
   */
  public MultiEvent getEventFromTemperature(double temp)
  {
    int true_variable = getInterval(temp);
    Valuation v = new Valuation();
    for (int i = 0; i < Math.ceil((s_maxTemp - s_minTemp) / s_interval); i++)
    {
      if (i == true_variable)
      {
        v.put("t" + i, Troolean.Value.TRUE);
      }
      else
      {
        v.put("t" + i, Troolean.Value.FALSE);
      }
    }
    return new ConcreteMultiEvent(v);
  }
  
  /**
   * Gets a multi-event corresponding to all valid temperature encodings
   * below some given threshold
   * @param temp The temperature threshold
   * @return The multi-event encoding thie temperature readings
   */
  public static ConcreteMultiEvent getTemperatureIsUnder(double min_temp, double max_temp, double interval, double temp)
  {
    int true_variable = getInterval(temp, min_temp, interval);
    int num_intervals = (int) Math.ceil((max_temp - min_temp) / interval);
    Set<Valuation> valuations = new HashSet<Valuation>();
    for (int i = 0; i < true_variable; i++)
    {
      Valuation v = new Valuation();
      for (int j = 0; j < num_intervals; j++)
      {
        if (j == i)
        {
          v.put("t" + j, Troolean.Value.TRUE);
        }
        else
        {
          v.put("t" + j, Troolean.Value.FALSE);
        }
      }
      valuations.add(v);
    }
    return new ConcreteMultiEvent(valuations);
  }
  
  /**
   * Gets a multi-event corresponding to all valid temperature encodings
   * above some given threshold
   * @param temp The temperature threshold
   * @return The multi-event encoding thie temperature readings
   */
  public static ConcreteMultiEvent getTemperatureIsOver(double min_temp, double max_temp, double interval, double temp)
  {
    int true_variable = getInterval(temp, min_temp, interval);
    int num_intervals = (int) Math.ceil((max_temp - min_temp) / interval);
    Set<Valuation> valuations = new HashSet<Valuation>();
    for (int i = true_variable; i < num_intervals; i++)
    {
      Valuation v = new Valuation();
      for (int j = 0; j < num_intervals; j++)
      {
        if (j == i)
        {
          v.put("t" + j, Troolean.Value.TRUE);
        }
        else
        {
          v.put("t" + j, Troolean.Value.FALSE);
        }
      }
      valuations.add(v);
    }
    return new ConcreteMultiEvent(valuations);
  }
  
  /**
   * Gets the names of all the variables used to encode temperatures.
   * @return An array containing the names of all the variables
   */
  public String[] getVariables()
  {
    return getVariables(s_minTemp, s_maxTemp, s_interval);
  }
  
  /**
   * Gets the names of all the variables used to encode temperatures.
   * @param min_temp The minimum temperature
   * @param max_temp The maximum temperature
   * @param interval The interval covered by each variable
   * @return An array containing the names of all the variables
   */
  public static String[] getVariables(float min_temp, float max_temp, float interval)
  {
    int num_intervals = (int) Math.ceil((max_temp - min_temp) / interval);
    String[] names = new String[num_intervals];
    for (int i = 0; i < num_intervals; i++)
    {
      names[i] = "t" + i;
    }
    return names;
  }
  
  /**
   * Gets the interval number corresponding to a numerical value.
   * @param value The value
   * @return The interval number
   */
  protected int getInterval(double value)
  {
    return getInterval(value, (double) s_minTemp, (double) s_interval);
  }
  
  /**
   * Gets the interval number corresponding to a numerical value.
   * @param value The value
   * @param min_temp The minimum temperature
   * @param interval_width The width of an interval
   * @return The interval number
   */
  public static int getInterval(double value, double min_temp, double interval_width)
  {
    return (int) ((value - min_temp) / interval_width);
  }
  
  /**
   * Produces the concrete multi-event containing all valuations where zero,
   * or more than one variable is true.
   * @return The multi-event  
   */
  public ConcreteMultiEvent getNotOneTrue()
  {
    return getNotOneTrue(s_minTemp, s_maxTemp, s_interval);
  }
  
  /**
   * Produces the concrete multi-event containing all valuations where zero,
   * or more than one variable is true.
   * @param min_temp The minimum temperature
   * @param max_temp The maximum temperature
   * @param interval_width The width of an interval
   * @return The multi-event  
   */
  public static ConcreteMultiEvent getNotOneTrue(float min_temp, float max_temp, float interval_width)
  {
    Set<Valuation> vals = new HashSet<Valuation>();
    ValuationIterator vi = new ValuationIterator(getVariables(min_temp, max_temp, interval_width));
    while (vi.hasNext())
    {
      Valuation v = vi.next();
      int true_cnt = 0;
      for (Map.Entry<String,Troolean.Value> e : v.entrySet())
      {
        if (e.getValue() == Troolean.Value.TRUE)
        {
          true_cnt++;
          if (true_cnt > 1)
          {
            break;
          }
        }
      }
      if (true_cnt == 0 || true_cnt > 1)
      {
        vals.add(v);
      }
    }
    return new ConcreteMultiEvent(vals);
  }
  
  /**
   * Produces the concrete multi-event containing all valuations where zero,
   * the temperature variable is over the threshold.
   * @param min_temp The minimum temperature
   * @param max_temp The maximum temperature
   * @param interval_width The width of an interval
   * @return The multi-event  
   */
  public static ConcreteMultiEvent getOverThreshold(float min_temp, float max_temp, float interval_width)
  {
    Set<Valuation> vals = new HashSet<Valuation>();
    ValuationIterator vi = new ValuationIterator(getVariables(min_temp, max_temp, interval_width));
    while (vi.hasNext())
    {
      Valuation v = vi.next();
      int true_cnt = 0;
      for (Map.Entry<String,Troolean.Value> e : v.entrySet())
      {
        if (e.getValue() == Troolean.Value.TRUE)
        {
          true_cnt++;
          if (true_cnt > 1)
          {
            break;
          }
        }
      }
      if (true_cnt == 0 || true_cnt > 1)
      {
        vals.add(v);
      }
    }
    return new ConcreteMultiEvent(vals);
  }

}
