package org.firstinspires.ftc.teamcode.opmode.collectorTest;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.day1Collector.CollectorChallengeAnswerKey;

//@Disabled // makes it so this doesn't show up in the driver station opMode dropdown
@TeleOp(name="Collector Test", group="Answer Key")
public class CollectorTestAnswerKey extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        // allows the telemetry to show on both FTC dashboard and on the driver station
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        // sets the refresh rate of the telemetry to be 20 Hz (loop times are 20ms, so it will refresh 50x per second)
        // default is much lower, so this allows for more responsive telemetry
        telemetry.setMsTransmissionInterval(20);

        CollectorChallengeAnswerKey collector = new CollectorChallengeAnswerKey(hardwareMap, telemetry);
        telemetry.addLine("Ready");
        telemetry.update();

        waitForStart();

        while(opModeIsActive()) {
            if(gamepad1.right_trigger > .1)
                collector.setIntakeState(CollectorChallengeAnswerKey.IntakeState.INTAKE);
            else if(gamepad1.left_trigger > .1)
                collector.setIntakeState(CollectorChallengeAnswerKey.IntakeState.OUTTAKE);
            else
                collector.setIntakeState(CollectorChallengeAnswerKey.IntakeState.OFF);

            collector.update();

            // the telemetry.update() should always go in the opMode
            telemetry.update();
        }
    }
}
