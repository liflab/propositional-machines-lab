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

import ca.uqac.lif.synthia.Picker;

public class RandomWalkInteger implements Picker<Integer>
{
  protected Picker<Integer> m_step;
  
  protected Picker<Integer> m_start;
  
  protected int m_min;
  
  protected int m_max;
  
  protected int m_currentValue;
  
  public RandomWalkInteger(int min, int max, Picker<Integer> start, Picker<Integer> step)
  {
    super();
    m_min = min;
    m_max = max;
    m_start = start;
    m_step = step;
    m_currentValue = start.pick();
  }
  
  @Override
  public RandomWalkInteger duplicate(boolean with_state)
  {
    RandomWalkInteger rwi = new RandomWalkInteger(m_min, m_max, m_start.duplicate(with_state), m_step.duplicate(with_state));
    if (with_state)
    {
      rwi.m_currentValue = m_currentValue;
    }
    return rwi;
  }

  @Override
  public Integer pick()
  {
    m_currentValue += m_step.pick();
    m_currentValue = Math.min(m_currentValue, m_max);
    m_currentValue = Math.max(m_currentValue, m_min);
    return m_currentValue;
  }

  @Override
  public void reset()
  {
    m_start.reset();
    m_step.reset();
    m_currentValue = m_start.pick();
  }

}
