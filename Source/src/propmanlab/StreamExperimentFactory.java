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

import ca.uqac.lif.cep.EventTracker;
import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.labpal.ExperimentFactory;
import ca.uqac.lif.labpal.Region;

import static propmanlab.StreamExperiment.PROPERTY;

/**
 * An {@link ExperimentFactory} that produces {@link StreamExperiment}s.
 */
@SuppressWarnings("rawtypes")
public class StreamExperimentFactory extends ExperimentFactory<MyLaboratory,StreamExperiment>
{
  public StreamExperimentFactory(MyLaboratory lab)
  {
    super(lab, StreamExperiment.class);
  }

  @Override
  protected StreamExperiment<?> createExperiment(Region r)
  {
    StreamExperiment<?> exp = new StreamExperiment();
    setSource(r, exp);
    exp.setEventStep(MyLaboratory.s_eventStep);
    Processor p = setProcessor(exp, r);
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
  
  @SuppressWarnings("unchecked")
  protected void setSource(Region r, StreamExperiment exp)
  {
    String property_name = r.getString(PROPERTY);
    if (property_name == null)
    {
      return;
    }
    // TODO set source
  }

  protected Processor setProcessor(StreamExperiment exp, Region r)
  {
    String property_name = r.getString(PROPERTY);
    Processor p = null;
    EventTracker t = null;
    exp.setTracker(t);
    if (property_name == null)
    {
      return null;
    }
    // TODO set processor
    exp.setProcessor(p);
    return p;
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
