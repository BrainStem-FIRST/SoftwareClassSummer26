package org.firstinspires.ftc.teamcode.opmode.colorSensorTest;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.day2ColorSensor.ColorSensorWrapperAnswerKey;

@TeleOp(name="ColorSensorTest")
public class ColorSensorTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        ColorSensorWrapperAnswerKey colorSensor = new ColorSensorWrapperAnswerKey(hardwareMap , telemetry);

        telemetry.addLine("Ready");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            colorSensor.update();

            telemetry.addData("seesColor", colorSensor.seesColorInRange());

            telemetry.update();
        }
    }
}
