package propmanlab.scenarios.simple;

import ca.uqac.lif.cep.propman.MergeVariables;
import ca.uqac.lif.cep.propman.MultiEventFactory;
import ca.uqac.lif.cep.propman.MultiEventFunction;
import ca.uqac.lif.cep.propman.PropositionalMachine;
import ca.uqac.lif.cep.propman.MultiEventFunction.Identity;

public class SimpleProxy extends PropositionalMachine
{
  public static final transient String NAME = "Propositional machine";
  
  public SimpleProxy()
  {
    MultiEventFunction f = new MergeVariables("a", "b");
    MultiEventFactory factory = new MultiEventFactory("a", "b", "c");
    addTransition(1, new Transition(2, factory.readFromValuations("TFF"), f));
    addTransition(1, new TransitionOtherwise(1, Identity.instance));
    addTransition(2, new Transition(1, factory.readFromValuations("FTF"), f));
    addTransition(2, new TransitionOtherwise(2, Identity.instance));
  }
}
