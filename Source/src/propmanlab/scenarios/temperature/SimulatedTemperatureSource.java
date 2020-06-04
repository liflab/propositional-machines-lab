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
