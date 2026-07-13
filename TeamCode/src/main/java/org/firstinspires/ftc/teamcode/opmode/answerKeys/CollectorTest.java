package org.firstinspires.ftc.teamcode.opmode.answerKeys;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.collector.CollectorChallengeAnswerKey;

@Disabled
//@TeleOp(name="Collector Test", group="Answer Key")
public class CollectorTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        telemetry.setMsTransmissionInterval(30);

        CollectorChallengeAnswerKey collector = new CollectorChallengeAnswerKey(hardwareMap);

        waitForStart();
        while(opModeIsActive()) {
            if(gamepad1.right_trigger > .1)
                collector.setIntakeState(CollectorChallengeAnswerKey.IntakeState.INTAKE);
            else if(gamepad1.left_trigger > .1)
                collector.setIntakeState(CollectorChallengeAnswerKey.IntakeState.OUTTAKE);
            else
                collector.setIntakeState(CollectorChallengeAnswerKey.IntakeState.OFF);

            collector.printTelemetry(telemetry);
            telemetry.update();
        }
    }
}
