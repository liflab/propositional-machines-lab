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
package propmanlab.scenarios.cpuload;

import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.cep.propman.MultiEvent;
import ca.uqac.lif.cep.propman.MultiEventFunction;
import ca.uqac.lif.cep.propman.PropositionalMachine;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import static ca.uqac.lif.cep.propman.MultiMonitor.EMPTY;
import static ca.uqac.lif.cep.propman.MultiMonitor.EPSILON;

public class CpuIsUnder extends PropositionalMachine
{
  /**
   * The width of the sliding window
   */
  protected int m_width;
  
  /**
   * The maximum sum tolerated in a window
   */
  protected int m_maxSum;
  
  /**
   * A mapping between integer values and the multi-event that
   * encodes this value
   */
  protected Map<Integer,MultiEvent> m_encodedEvents;
  
  /**
   * 
   * @param threshold The average threshold the CPU load must not exceed
   * @param window_width The width of the sliding window
   */
  public CpuIsUnder(int threshold, int window_width)
  {
    super();
    m_width = window_width;
    m_maxSum = window_width * threshold;
    m_encodedEvents = new HashMap<Integer,MultiEvent>(101);
    for (int i = 0; i < 101; i++)
    {
      m_encodedEvents.put(i, CpuSource.getEventFromLoad(i));
    }
  }
  
  @Override
  public ValueWindow getInitialState()
  {
    return new ValueWindow();
  }

  @Override
  public List<Transition> getTransitionsFor(Object state)
  {
    ValueWindow src = (ValueWindow) state;
    List<Transition> list = new ArrayList<Transition>(101);
    for (int i = 0; i < 101; i++)
    {
      ValueWindow dest = src.slide(i);
      MultiEvent cond = m_encodedEvents.get(i);
      MultiEventFunction f = EPSILON;
      if (dest.getSum() > m_maxSum)
      {
        f = EMPTY;
      }
      list.add(new Transition(dest, cond, f));
    }
    return list;
  }

  @Override
  public long getStateCount()
  {
    long sum = 1;
    for (int i = 1; i <= m_width; i++)
    {
      sum += (int) Math.pow(101, i);
    }
    return sum;
  }

  @Override
  public long getTransitionCount()
  {
    return getStateCount() * 101;
  }

  @Override
  protected boolean compute(Object[] inputs, Queue<Object[]> outputs)
  {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Processor duplicate(boolean with_state)
  {
    // TODO Let's do this later
    throw new UnsupportedOperationException("Duplication is not supported for this processor");
  }
  
  /**
   * An abstract identifier for states in the form of a sliding window of
   * integer values.
   */
  public class ValueWindow
  {
    /**
     * The list of values in the window
     */
    protected List<Integer> m_values;
    
    /**
     * Creates a new empty window
     */
    public ValueWindow()
    {
      super();
      m_values = new ArrayList<Integer>(m_width);
    }
    
    /**
     * Creates a new window with a predefined list of values
     * @param values The list of values
     */
    protected ValueWindow(List<Integer> values)
    {
      super();
      m_values = values;
    }
    
    /**
     * Creates a new window by sliding the current window with a new value
     * @param new_value The new value to be added to the window
     * @return The new window
     */
    public ValueWindow slide(int new_value)
    {
      List<Integer> new_values = new ArrayList<Integer>(m_width);
      new_values.add(new_value);
      if (new_values.size() == m_width + 1)
      {
        new_values.remove(0);
      }
      return new ValueWindow(new_values);
    }
    
    /**
     * Gets the sum of values in the window
     * @return The sum
     */
    public int getSum()
    {
      int sum = 0;
      for (int v : m_values)
      {
        sum += v;
      }
      return sum;
    }
    
    @Override
    public int hashCode()
    {
      if (m_values.isEmpty())
      {
        return -1;
      }
      return m_values.get(0);
    }
    
    @Override
    public boolean equals(Object o)
    {
      if (o == null || !(o instanceof ValueWindow))
      {
        return false;
      }
      ValueWindow vw = (ValueWindow) o;
      if (vw.m_values.size() != m_values.size())
      {
        return false;
      }
      for (int i = 0; i < m_values.size(); i++)
      {
        if (m_values.get(i) != vw.m_values.get(i))
        {
          return false;
        }
      }
      return true;
    }
    
    @Override
    public String toString()
    {
      return m_values.toString();
    }
  }
}
