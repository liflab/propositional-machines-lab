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
package propmanlab.scenarios.mplayer;

import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.cep.propman.ExplicitPropositionalMachine;
import ca.uqac.lif.labpal.Region;
import ca.uqac.lif.synthia.Picker;
import propmanlab.AccessControlledStreamExperiment;
import propmanlab.MyLaboratory;
import propmanlab.scenarios.RandomScenario;

public class MPlayerScenario extends RandomScenario<Float>
{
  public static final transient String NAME = "MPlayer";
  
  public MPlayerScenario(Picker<Float> float_source)
  {
    super(NAME, "MPlayerSource", "Load shedding", "Property 1", float_source);
  }
  
  @Override
  public Processor getSource(AccessControlledStreamExperiment e, Region r)
  {
    MPlayerSource src = new MPlayerSource(MyLaboratory.MAX_TRACE_LENGTH, m_picker);
    e.setSource(src);
    return src;
  }

  @Override
  public ExplicitPropositionalMachine getProxyInstance(AccessControlledStreamExperiment e, Region r)
  {
    MPlayerProxy proxy = new MPlayerProxy(20, 15);
    e.setProxy(proxy);
    return proxy;
  }

  @Override
  public ExplicitPropositionalMachine getMonitor(AccessControlledStreamExperiment e, Region r)
  {
    Property1 mon = new Property1();
    e.setMonitor(mon);
    return mon;
  }
}
