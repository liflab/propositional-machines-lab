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
import ca.uqac.lif.labpal.LatexNamer;
import ca.uqac.lif.labpal.macro.MacroScalar;
import propmanlab.AccessControlledStreamExperiment;
import propmanlab.MyLaboratory;
import propmanlab.scenarios.Scenario;

public class UniTraceCount extends MacroScalar
{
  protected transient AccessControlledStreamExperiment m_experiment;
  
  public UniTraceCount(MyLaboratory lab, AccessControlledStreamExperiment e)
  {
    super(lab, LatexNamer.latexify("nbUniTraces" + e.readString(Scenario.SCENARIO)));
    setDescription("The number of uni-traces processed by the multi-monitor in the scenario " + e.readString(Scenario.SCENARIO));
    m_experiment = e;
  }
  
  @Override
  public JsonNumber getValue()
  {
    JsonNumber j_n = (JsonNumber) m_experiment.read(AccessControlledStreamExperiment.NB_UNITRACES);
    return j_n;
  }
}
