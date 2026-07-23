package org.firstinspires.ftc.teamcode.utils.misc;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;

/**
 * Low-pass filter over the raw voltage sensor reading.
 * Raw battery voltage is VERY noisy so feeding it
 * straight into a feedforward calculation would make motor power jump around. Exponential
 * smoothing trades a bit of responsiveness for a much steadier signal.
 * THE POWER OF AVERAGES
 */
public class BatteryVoltageFilter {
    // not meant to be used in FTC dashboard
    private static final double initialVoltageGuess = 13;
    // weight given to the previous filtered value each update; higher alpha = smoother but slower to react
    private static final double alpha = .8;

    private final VoltageSensor voltageSensor;

    private double voltage;

    public BatteryVoltageFilter(HardwareMap hardwareMap) {
        voltageSensor = hardwareMap.voltageSensor.iterator().next();
        // start from a guess instead of 0 so the very first reads aren't way off before the filter settles
        voltage = initialVoltageGuess;
    }

    /**
     * Reads the current raw voltage and blends it into the filtered value.
     * Call this once per loop iteration before calling getVoltage().
     */
    public void update() {
        double rawVoltage = voltageSensor.getVoltage();
        voltage = voltage * alpha + rawVoltage * (1 - alpha);
    }

    /** Returns the filtered battery voltage. */
    public double getVoltage() {
        return voltage;
    }
}