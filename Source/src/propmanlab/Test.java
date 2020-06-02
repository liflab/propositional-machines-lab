package propmanlab;

import ca.uqac.lif.cep.Pullable;
import ca.uqac.lif.cep.propman.MultiEvent;
import ca.uqac.lif.labpal.Random;
import ca.uqac.lif.synthia.random.RandomBoolean;
import ca.uqac.lif.synthia.random.RandomInteger;
import propmanlab.scenarios.temperature.BlurTemperature;
import propmanlab.scenarios.temperature.TemperatureSource;
import propmanlab.source.RandomConcreteMultiEventSource;

public class Test
{

  public static void main(String[] args)
  {
    //RandomConcreteMultiEventSource source = new RandomConcreteMultiEventSource(new Random(), 10, new RandomInteger(0, 4), new RandomBoolean(), "a", "b");
    TemperatureSource source = new TemperatureSource(20);
    Pullable p = source.getPullableOutput();
    /*while (p.hasNext())
    {
      System.out.println(p.next());
    }*/
    MultiEvent in_e = source.getEventFromTemperature(75);
    BlurTemperature blur = new BlurTemperature(1, 96);
    System.out.println(blur.getValue(in_e));
    
  }

}
