package org.firstinspires.ftc.teamcode.subsystems.day2ColorSensor;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class ColorSensorWrapperAnswerKey {
    public static double redMin = 0, redMax = 0;
    public static double greenMin = 0, greenMax = 0;
    public static double blueMin = 0, blueMax = 0;

    private final Telemetry telemetry;
    private final ColorSensor colorSensor;

    private double redNorm, greenNorm, blueNorm;

    public ColorSensorWrapperAnswerKey(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;
        colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");
    }

    public void update() {
        // saved values from color sensor from update function
        double redRaw = colorSensor.red();
        double greenRaw = colorSensor.green();
        double blueRaw = colorSensor.blue();

        double sum = redRaw + greenRaw + blueRaw;
        redNorm = redRaw / sum;
        greenNorm = greenRaw / sum;
        blueNorm = blueRaw / sum;

        telemetry.addData("redRaw", redRaw);
        telemetry.addData("greenRaw", greenRaw);
        telemetry.addData("blueRaw", blueRaw);
        telemetry.addLine();
        telemetry.addData("redNorm", redNorm);
        telemetry.addData("greenNorm", greenNorm);
        telemetry.addData("blueNorm", blueNorm);
    }


    public boolean seesColorInRange() {
        return redMin <= redNorm && redNorm <= redMax
                && greenMin <= greenNorm && greenNorm <= greenMax
                && blueMin <= blueNorm && blueNorm <= blueMax;
    }
}
