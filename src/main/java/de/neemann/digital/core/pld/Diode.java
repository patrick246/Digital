package de.neemann.digital.core.pld;

import de.neemann.digital.core.*;
import de.neemann.digital.core.element.Element;
import de.neemann.digital.core.element.ElementAttributes;
import de.neemann.digital.core.element.ElementTypeDescription;
import de.neemann.digital.core.element.Keys;

/**
 * A diode needed to create wired elements
 * Used to build a wired OR or AND.
 */
public class Diode implements Element, NodeInterface {

    /**
     * The diodes description
     */
    public static final ElementTypeDescription DESCRIPTION = new ElementTypeDescription(Diode.class)
            .addAttribute(Keys.ROTATE)
            .addAttribute(Keys.BLOWN);

    private final ObservableValue cathode;
    private final ObservableValue anode;
    private final boolean blown;
    private ObservableValue cathodeIn;
    private ObservableValue anodeIn;

    /**
     * Creates a new instance
     *
     * @param attr the elements attributes
     */
    public Diode(ElementAttributes attr) {
        cathode = new ObservableValue("cathode", 1, true).setBidirectional();
        anode = new ObservableValue("anode", 1, true).setBidirectional();
        blown = attr.get(Keys.BLOWN);
    }

    @Override
    public void setInputs(ObservableValues inputs) throws NodeException {
        cathodeIn = inputs.get(0).addObserverToValue(this).checkBits(1, null);
        anodeIn = inputs.get(1).addObserverToValue(this).checkBits(1, null);
    }

    @Override
    public ObservableValues getOutputs() {
        return new ObservableValues(cathode, anode);
    }

    @Override
    public void registerNodes(Model model) {
        // its just a wire and has no delay, so it is'nt a node
    }

    @Override
    public void hasChanged() {
        if (!blown) {
            if (anodeIn.getBool() && !anodeIn.isHighZ())
                cathode.set(1, false);
            else
                cathode.set(1, true);
            if (!cathodeIn.getBool() && !cathodeIn.isHighZ())
                anode.set(0, false);
            else
                anode.set(0, true);
        }
    }

    @Override
    public void init(Model model) throws NodeException {
        hasChanged();
    }
}
