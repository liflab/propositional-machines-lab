package propmanlab.scenarios.simple;

import static ca.uqac.lif.cep.propman.MultiMonitor.EMPTY;
import static ca.uqac.lif.cep.propman.MultiMonitor.EPSILON;
import static ca.uqac.lif.cep.propman.MultiMonitor.NU;

import ca.uqac.lif.cep.propman.MultiEventFactory;
import ca.uqac.lif.cep.propman.PropositionalMachine;

public class SimpleMonitor extends PropositionalMachine
{
  public static final transient String NAME = "Simple Monitor";
  
  public SimpleMonitor()
  {
    String[] variables = new String[] {"a", "b", "c"};
    MultiEventFactory factory = new MultiEventFactory(variables);
    addTransition(1, new Transition(2, factory.readFromValuations("TFF"), EPSILON));
    addTransition(1, new Transition(3, factory.readFromValuations("FTF"), EPSILON));
    addTransition(1, new TransitionOtherwise(1, EPSILON));
    addTransition(2, new Transition(4, factory.readFromValuations("TFF"), EPSILON));
    addTransition(2, new TransitionOtherwise(2, EPSILON));
    addTransition(3, new Transition(6, factory.readFromValuations("FTF"), NU));
    addTransition(3, new TransitionOtherwise(1, EPSILON));
    addTransition(4, new TransitionOtherwise(5, EMPTY));
    addTransition(5, new TransitionOtherwise(5, EMPTY));
    addTransition(6, new TransitionOtherwise(6, NU));
  }
}
