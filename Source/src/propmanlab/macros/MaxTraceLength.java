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
package propmanlab.macros;

import ca.uqac.lif.json.JsonNumber;
import ca.uqac.lif.labpal.Laboratory;
import ca.uqac.lif.labpal.macro.MacroScalar;

public class MaxTraceLength extends MacroScalar
{
  protected int m_length;
  
  public MaxTraceLength(Laboratory lab, int length)
  {
    super(lab, "maxTraceLen");
    setDescription("The maximum trace length used in the experiments");
    m_length = length;
  }
  
  @Override
  public JsonNumber getValue()
  {
    return new JsonNumber(m_length);
  }

}
