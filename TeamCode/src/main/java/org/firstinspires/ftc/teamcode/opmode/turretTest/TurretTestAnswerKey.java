package org.firstinspires.ftc.teamcode.opmode.turretTest;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.subsystems.day1Turret.TurretAnswerKey;

@Config
@Disabled // makes it so this doesn't show up in the driver station opMode dropdown
//@TeleOp(name="Turret Test", group="Answer Key")
public class TurretTestAnswerKey extends LinearOpMode {
    // set this from FTC Dashboard to choose what angle the turret points at
    public static double targetAngle = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        // allows the telemetry to show on both FTC dashboard and on the driver station
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        // sets the refresh rate of the telemetry to be 50 Hz (loop times are 20ms, so it will refresh 50x per second)
        // default is much lower, so this allows for more responsive telemetry
        telemetry.setMsTransmissionInterval(20);

        TurretAnswerKey turret = new TurretAnswerKey(hardwareMap, telemetry);
        telemetry.addLine("Ready");
        telemetry.update();

        waitForStart();

        while(opModeIsActive()) {
            // targetAngle is set live from FTC dashboard, so update it every loop
            turret.setTargetAngle(targetAngle);

            if(gamepad1.a)
                turret.setTurretState(TurretAnswerKey.TurretState.OFF);
            else if(gamepad1.b)
                turret.setTurretState(TurretAnswerKey.TurretState.POINT_AT_ANGLE);
            else if(gamepad1.x)
                turret.setTurretState(TurretAnswerKey.TurretState.SWING_PAST_ANGLE);

            turret.update();

            // the telemetry.update() should always go in the opMode
            telemetry.update();
        }
    }
}
