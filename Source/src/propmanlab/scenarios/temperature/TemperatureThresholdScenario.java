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

import static propmanlab.AccessControlledStreamExperiment.BEST_EFFORT;
import static propmanlab.AccessControlledStreamExperiment.PROXY;

import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.cep.propman.ExplicitPropositionalMachine;
import ca.uqac.lif.cep.propman.StatelessPropositionalMachine;
import ca.uqac.lif.cep.propman.SymbolicMultiEvent;
import ca.uqac.lif.json.JsonFalse;
import ca.uqac.lif.json.JsonTrue;
import ca.uqac.lif.labpal.Region;
import ca.uqac.lif.synthia.Picker;
import ca.uqac.lif.synthia.random.AffineTransform.AffineTransformFloat;
import propmanlab.AccessControlledStreamExperiment;
import propmanlab.scenarios.RandomScenario;

public class TemperatureThresholdScenario extends RandomScenario<Float>
{
  public static final transient String NAME = "Tempreature Threshold";

  public TemperatureThresholdScenario(Picker<Float> picker)
  {
    super(NAME, "Random", "Blur temperature (stateless)", "Temperature threshold within 100", picker);
  }

  @Override
  public Processor getSource(AccessControlledStreamExperiment e, Region r)
  {
    Picker<Float> picker = new AffineTransformFloat(m_picker, 20, 70);
    RandomTemperatureSource source = new RandomTemperatureSource(70, 90, 1, picker);
    e.setSource(source);
    return source;
  }

  @Override
  public ExplicitPropositionalMachine getProxyInstance(AccessControlledStreamExperiment e, Region r)
  {
    ExplicitPropositionalMachine p = null;
    if (!r.hasDimension(BEST_EFFORT) || r.get(BEST_EFFORT) instanceof JsonFalse)
    {
      StatelessPropositionalMachine pm = new StatelessPropositionalMachine();
      pm.addCondition(SymbolicMultiEvent.ALL, new BlurTemperature(2));
      e.setInput(PROXY, "Propositional machine");
      e.setInput(BEST_EFFORT, JsonFalse.instance);
      p = pm;
    }
    else
    {
      StatelessPropositionalMachine pm = new StatelessPropositionalMachine();
      pm.addCondition(SymbolicMultiEvent.ALL, new BlurTemperatureApproximated(2));
      e.setInput(PROXY, "Best effort");
      e.setInput(BEST_EFFORT, JsonTrue.instance);
      p = pm;
    }
    e.setProxy(p);
    return p;
  }

  @Override
  public ExplicitPropositionalMachine getMonitor(AccessControlledStreamExperiment e, Region r)
  {
    int threshold = 90;
    TemperatureIsUnder tis = new TemperatureIsUnder(threshold, 70, 1);
    OverThresholdWithinInterval otwi = new OverThresholdWithinInterval(tis, TemperatureSource.getTemperatureIsOver(TemperatureSource.s_minTemp, TemperatureSource.s_maxTemp, TemperatureSource.s_interval, threshold), 100, 5);
    e.setMonitor(otwi);
    return otwi;
  }

}
