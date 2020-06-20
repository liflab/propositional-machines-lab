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
package propmanlab;

import ca.uqac.lif.labpal.ExperimentFactory;
import ca.uqac.lif.labpal.Region;
import java.util.HashMap;
import java.util.Map;
import propmanlab.scenarios.Scenario;

import static propmanlab.scenarios.Scenario.SCENARIO;

/**
 * An {@link ExperimentFactory} that produces {@link StreamExperiment}s.
 */
public class StreamExperimentFactory extends ExperimentFactory<MyLaboratory,AccessControlledStreamExperiment>
{
  protected Map<String,Scenario> m_scenarios;

  public StreamExperimentFactory(MyLaboratory lab)
  {
    super(lab, AccessControlledStreamExperiment.class);
    m_scenarios = new HashMap<String,Scenario>();
  }

  public StreamExperimentFactory addScenario(String name, Scenario s)
  {
    m_scenarios.put(name, s);
    return this;
  }

  @Override
  protected AccessControlledStreamExperiment createExperiment(Region r)
  {
    String scenario_s = r.getString(SCENARIO);
    if (!m_scenarios.containsKey(scenario_s))
    {
      return null;
    }
    Scenario scenario = m_scenarios.get(scenario_s);
    if (scenario == null)
    {
      return null;
    }
    AccessControlledStreamExperiment exp = new AccessControlledStreamExperiment();
    scenario.setup(exp, r);
    exp.setEventStep(MyLaboratory.s_eventStep);
    return exp;
  }
}
