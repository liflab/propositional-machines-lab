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
import ca.uqac.lif.cep.propman.PropositionalMachine;
import ca.uqac.lif.cep.propman.StatelessPropositionalMachine;
import ca.uqac.lif.cep.propman.SymbolicMultiEvent;
import ca.uqac.lif.labpal.Region;
import ca.uqac.lif.synthia.Picker;
import propmanlab.AccessControlledStreamExperiment;
import propmanlab.scenarios.RandomScenario;

public class CpuLoadThresholdScenario extends RandomScenario<Integer>
{
  public static final transient String NAME = "CPU load";
  
  public CpuLoadThresholdScenario(Picker<Integer> picker)
  {
    super(NAME, "Random", "Blur load (stateless)", "CPU load threshold", picker);
  }
  
  @Override
  public Processor getSource(AccessControlledStreamExperiment e, Region r)
  {
    RandomCpuSource source = new RandomCpuSource(m_picker);
    e.setSource(source);
    return source;
  }

  @Override
  public PropositionalMachine getProxyInstance(AccessControlledStreamExperiment e, Region r)
  {
    StatelessPropositionalMachine pm = new StatelessPropositionalMachine();
    pm.addCondition(SymbolicMultiEvent.ALL, new BlurLoad(1));
    e.setProxy(pm);
    return pm;
  }

  @Override
  public PropositionalMachine getMonitor(AccessControlledStreamExperiment e, Region r)
  {
    CpuIsUnder tis = new CpuIsUnder(90, 5);
    e.setMonitor(tis);
    return tis;
  }

}
