package de.neemann.digital.core;

import de.neemann.digital.core.wiring.Clock;
import de.neemann.digital.lang.Lang;

import java.util.ArrayList;

/**
 * This class performs a speed test, to determine how fast the model can run.
 *
 * @author hneemann
 */
public class SpeedTest {
    private static final int LOOPCOUNTER = 10000;
    private final Model model;

    /**
     * Creates a new Speed test instance
     *
     * @param model the model to measure
     */
    public SpeedTest(Model model) {
        this.model = model;
    }

    /**
     * Calculates and returns the maximal frequency in Hz
     *
     * @return the maximal frequency in Hz
     * @throws NodeException NodeException
     */
    public int calculate() throws NodeException {
        ArrayList<Clock> clocks = model.getClocks();
        if (clocks.isEmpty())
            throw new NodeException(Lang.get("err_noClockFound"));
        else if (clocks.size() > 1)
            throw new NodeException(Lang.get("err_moreThenOneClocksFound"));


        Clock clock = clocks.get(0);
        model.init();
        ObservableValue clockValue = clock.getOutputs().get(0);
        int state = (int) clockValue.getValue();

        long aktTime;
        long starTime = System.currentTimeMillis();
        int loops = 0;
        do {
            for (int i = 0; i < LOOPCOUNTER; i++) {
                state = 1 - state;
                clockValue.setValue(state);
                model.doStep();
            }
            loops++;
            aktTime = System.currentTimeMillis();
        } while (aktTime - starTime < 1000);

        long cycles = ((long) loops) * LOOPCOUNTER / 2;
        double time = (aktTime - starTime) / 1000.0;

        double freqency = cycles / time;

        System.out.println("cycles: " + cycles);
        System.out.println("time  : " + time + "s");
        System.out.println("freq  :" + freqency);

        return (int) freqency;
    }
}
