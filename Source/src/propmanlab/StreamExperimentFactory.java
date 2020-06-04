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

import ca.uqac.lif.cep.propman.PropositionalMachine;
import ca.uqac.lif.json.JsonFalse;
import ca.uqac.lif.json.JsonTrue;
import ca.uqac.lif.labpal.ExperimentFactory;
import ca.uqac.lif.labpal.Region;
import propmanlab.scenarios.simple.SimpleMonitor;
import propmanlab.scenarios.simple.SimpleProxy;

import static propmanlab.AccessControlledStreamExperiment.PROXY;
import static propmanlab.AccessControlledStreamExperiment.WITH_PROXY;
import static propmanlab.StreamExperiment.PROPERTY;

/**
 * An {@link ExperimentFactory} that produces {@link StreamExperiment}s.
 */
@SuppressWarnings("rawtypes")
public class StreamExperimentFactory extends ExperimentFactory<MyLaboratory,AccessControlledStreamExperiment>
{
  public StreamExperimentFactory(MyLaboratory lab)
  {
    super(lab, AccessControlledStreamExperiment.class);
  }

  @Override
  protected AccessControlledStreamExperiment createExperiment(Region r)
  {
    AccessControlledStreamExperiment exp = new AccessControlledStreamExperiment();
    setSource(r, exp);
    exp.setEventStep(MyLaboratory.s_eventStep);
    PropositionalMachine p = setProcessorAndProxy(exp, r);
    if (p == null)
    {
      return null;
    }
    exp.setInput(PROPERTY, r.getString(PROPERTY));
    exp.setPropertyDescription(getPropertyDescription(r));
    exp.setImageUrl(getImageUrl(r));
    exp.setPredictedThroughput(guessThroughput(r));
    return exp;
  }

  protected void setSource(Region r, StreamExperiment exp)
  {
    String property_name = r.getString(PROPERTY);
    if (property_name == null)
    {
      return;
    }
    // TODO set source
  }

  protected PropositionalMachine setProcessorAndProxy(AccessControlledStreamExperiment exp, Region r)
  {
    String property_name = r.getString(PROPERTY);
    PropositionalMachine prop = null;
    switch (property_name)
    {
    case SimpleMonitor.NAME:
      prop = new SimpleMonitor();
      break;
    }
    if (prop == null)
    {
      return null;
    }
    PropositionalMachine proxy = null;
    if (r.get(WITH_PROXY) instanceof JsonTrue)
    {
      String proxy_name = r.getString(PROXY);
      switch (proxy_name)
      {
      case SimpleProxy.NAME:
        proxy = new SimpleProxy();
      }
    }
    exp.setProcessors(proxy, prop);
    return prop;
  }

  protected String getPropertyDescription(Region r)
  {
    String property_name = r.getString(PROPERTY);
    if (property_name == null)
    {
      return null;
    }
    // TODO set description
    return null;
  }

  protected String getProxyDescription(Region r)
  {
    if (r.get(WITH_PROXY) instanceof JsonFalse)
    {
      return null;
    }
    String proxy_name = r.getString(PROXY);
    if (proxy_name == null)
    {
      return null;
    }
    // TODO set description
    return null;
  }

  protected String getImageUrl(Region r)
  {
    String property_name = r.getString(PROPERTY);
    if (property_name == null)
    {
      return null;
    }
    /*if (property_name.compareTo(ParcelsInTransit.NAME) == 0)
    {
      // No picture for this one
      return "/resource/ParcelsInTransit.png";
    }*/
    return null;
  }

  /**
   * Estimates the throughput of an experiment.
   * These throughput values are rough estimates based on values
   * collected when running the experiments.
   * @param r The region representing an experiment
   * @return The estimated throughput
   */
  protected float guessThroughput(Region r)
  {
    String property_name = r.getString(PROPERTY);
    if (property_name == null)
    {
      return 0f;
    }
    /*if (property_name.compareTo(DecreasingDistance.NAME) == 0)
    {
      return 300000f;
    }*/
    return 0f;
  }


}
