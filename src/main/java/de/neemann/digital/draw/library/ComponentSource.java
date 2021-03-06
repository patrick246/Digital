package de.neemann.digital.draw.library;

/**
 * User has to implement a component source
 */
public interface ComponentSource {

    /**
     * User has to implement this interface in order to add components to Digital
     *
     * @param adder the ComponentManager
     * @throws InvalidNodeException thrown if node is invalid
     */
    void registerComponents(ComponentManager adder) throws InvalidNodeException;
}
