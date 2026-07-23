package org.firstinspires.ftc.teamcode.opmode.shooterTest;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.day2Shooter.Shooter;

@Config
@TeleOp(name="Shooter Test")
public class ShooterTest extends LinearOpMode {
    public static double targetVelocity = 0;
    public static double targetPower = 0;
    public static boolean useTargetPower = true;
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(FtcDashboard.getInstance().getTelemetry(), telemetry);
        telemetry.setMsTransmissionInterval(30);

        Shooter shooter = new Shooter(hardwareMap, telemetry);

        waitForStart();

        while(opModeIsActive()) {

            if(useTargetPower) {
                shooter.setShooterState(Shooter.ShooterState.CONSTANT_POWER);
                shooter.setTargetPower(targetPower);
            }
            else {
                shooter.setShooterState(Shooter.ShooterState.VELOCITY_CONTROL);
                shooter.setTargetVelocity(targetVelocity);
            }

            shooter.update();

            telemetry.update();
        }

    }
}
