package propmanlab;

import ca.uqac.lif.cep.Pullable;
import ca.uqac.lif.labpal.Random;
import ca.uqac.lif.synthia.random.RandomBoolean;
import ca.uqac.lif.synthia.random.RandomInteger;
import propmanlab.source.RandomConcreteMultiEventSource;

public class Test
{

  public static void main(String[] args)
  {
    RandomConcreteMultiEventSource source = new RandomConcreteMultiEventSource(new Random(), 10, new RandomInteger(0, 4), new RandomBoolean(), "a", "b");
    Pullable p = source.getPullableOutput();
    while (p.hasNext())
    {
      System.out.println(p.next());
    }
  }

}
