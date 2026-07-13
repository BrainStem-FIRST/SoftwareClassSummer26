package org.firstinspires.ftc.teamcode.opmode.answerKeys;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Config
@TeleOp(name="PID Tuning", group="Answer Key")
public class PIDTuning extends LinearOpMode {
    public static double kP = 0, kI = 0, kD = 0;
    public static double setPoint = 0;
    public static double radiansPerEncoder = 0;
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        telemetry.setMsTransmissionInterval(30);

        PIDController pid = new PIDController(kP, kI, kD);
        DcMotorEx turret = hardwareMap.get(DcMotorEx.class, "turret");
        // resetting turret encoder
        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // this is so that we can actually set power to turret again after resetting encoder
        turret.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        waitForStart();

        while(opModeIsActive()) {
            int turretEncoder = turret.getCurrentPosition();
            pid.setPID(kP, kI, kD);
            pid.setSetPoint(setPoint);
            double power = pid.calculate(turretEncoder);
            turret.setPower(power);

            telemetry.addData("encoder", turretEncoder);
            telemetry.addData("angle", turretEncoder * radiansPerEncoder);
            telemetry.addData("setpoint", setPoint);
            telemetry.addData("power", power);
            telemetry.update();
        }
    }
}
