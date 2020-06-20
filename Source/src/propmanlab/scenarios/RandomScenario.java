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
package propmanlab.scenarios;

import ca.uqac.lif.synthia.Picker;

public abstract class RandomScenario<T> extends Scenario
{
  /**
   * A source of randomness
   */
  protected Picker<T> m_picker;
  
  public RandomScenario(String name, String source_name, String proxy_name, String monitor_name, Picker<T> picker)
  {
    super(name, source_name, proxy_name, monitor_name);
    m_picker = picker;
  }
}
