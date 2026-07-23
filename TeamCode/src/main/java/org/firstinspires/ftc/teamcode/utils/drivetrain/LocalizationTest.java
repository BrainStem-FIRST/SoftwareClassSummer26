package org.firstinspires.ftc.teamcode.utils.drivetrain;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Config
@TeleOp(name="Localization Test")
public class LocalizationTest extends LinearOpMode {
    public static double startX = 0, startY = 0, startA = 0;
    @Override
    public void runOpMode() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(startX, startY, startA));

        waitForStart();

        while (opModeIsActive()) {
            // why is linearPowers declared as (-y, -x) instead of (x, y)????? figure it out
            Vector2d linearPowers = new Vector2d(-gamepad1.left_stick_y, -gamepad1.left_stick_x);
            double turnPower = -gamepad1.right_stick_x;
            drive.setDrivePowers(new PoseVelocity2d(linearPowers, turnPower));

            drive.updatePoseEstimate();

            Pose2d pose = drive.pinpoint().getPose();
            telemetry.addData("x", pose.position.x);
            telemetry.addData("y", pose.position.y);
            telemetry.addData("heading (deg)", Math.toDegrees(pose.heading.toDouble()));
            telemetry.update();

            TelemetryPacket packet = new TelemetryPacket();
            packet.fieldOverlay().setStroke("#3F51B5");
            Drawing.drawRobot(packet.fieldOverlay(), pose);
            FtcDashboard.getInstance().sendTelemetryPacket(packet);
        }
    }
}
