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

import java.util.ArrayList;
import java.util.List;

import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.cep.propman.MultiEvent;
import propmanlab.source.MultiEventSource;

/**
 * Generates a pre-programmed stream of temperature values.
 */
public class SimulatedTemperatureSource extends MultiEventSource
{
  protected List<Float> m_readings;

  protected float m_minTemp;

  protected float m_maxTemp;

  protected float m_interval;

  protected int m_index = 0;

  public SimulatedTemperatureSource(float min_temp, float max_temp, int interval, List<Float> readings)
  {
    super(readings.size() - 1, TemperatureSource.getVariables(min_temp, max_temp, interval));
    m_minTemp = min_temp;
    m_maxTemp = max_temp;
    m_interval = interval;
    m_readings = readings;
  }

  public SimulatedTemperatureSource(float min_temp, float max_temp, int interval, float ... readings)
  {
    super(readings.length - 1, TemperatureSource.getVariables(min_temp, max_temp, interval));
    m_readings = new ArrayList<Float>(readings.length);
    for (float f : readings)
    {
      m_readings.add(f);
    }
    m_minTemp = min_temp;
    m_maxTemp = max_temp;
    m_interval = interval;
  }

  @Override
  protected MultiEvent getEvent()
  {
    return TemperatureSource.getEventFromTemperature(m_readings.get(m_index++), m_minTemp, m_maxTemp, m_interval);
  }

  @Override
  public String getFilename()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Processor duplicate(boolean with_state)
  {
    // TODO Auto-generated method stub
    return null;
  }  
}
