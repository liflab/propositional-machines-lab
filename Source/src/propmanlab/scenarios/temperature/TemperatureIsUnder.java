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

import ca.uqac.lif.cep.ltl.Troolean;
import ca.uqac.lif.cep.propman.PropositionalFormula;
import ca.uqac.lif.cep.propman.PropositionalVariable;
import ca.uqac.lif.cep.propman.SymbolicMultiEvent;

/**
 * Propositional formula asserting that the temperature is under a given
 * threshold value.
 */
public class TemperatureIsUnder extends SymbolicMultiEvent
{

  /**
   * Creates a new instance of the formula
   * @param temp The temperature threshold
   * @param min_temp The minimum temperature used in the Boolean encoding
   * @param interval_width The width of a temperature interval
   */
  public TemperatureIsUnder(float temp, float min_temp, float interval_width)
  {
    super(getFormula(temp, min_temp, interval_width));
  }
  
  /**
   * Gets the Boolean formula representing the condition
   * @param temp The temperature threshold
   * @param min_temp The minimum temperature used in the Boolean encoding
   * @param interval_width The width of a temperature interval
   * @return The Boolean formula
   */
  protected static PropositionalFormula getFormula(float temp, float min_temp, float interval_width)
  {
    PropositionalFormula big_or = new PropositionalFormula(Troolean.OR_FUNCTION);
    int max_interval = Math.max(0, TemperatureSource.getInterval(temp, min_temp, interval_width));
    if (max_interval == 0)
    {
      return new PropositionalFormula(PropositionalVariable.get("t0"));
    }
    for (int i = 0; i < max_interval; i++)
    {
      if (i == 0)
      {
        big_or.setChild(0, PropositionalVariable.get("t" + i));
      }
      else
      {
        big_or.setChild(1, PropositionalVariable.get("t" + i));
      }
      if (i > 0 && i < max_interval - 1)
      {
        // Not last loop
        PropositionalFormula new_or = new PropositionalFormula(Troolean.OR_FUNCTION);
        new_or.setChild(0, big_or);
        big_or = new_or;
      }
    }
    return big_or;
  }
}
