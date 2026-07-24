package org.firstinspires.ftc.teamcode.subsystems.day4Classes;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class Arm extends SubsystemBase {
    // when first coding subsystems, you can leave these tuning values 0
    // (just remember that you have to tune them later on)
    public static double kG = 0, kS = 0;
    public static double kP = 0, kI = 0, kD = 0;
    public static double radiansPerEncoder = 0;
    public static double deadbandRadians;

    private final DcMotorEx armMotor;
    private final Telemetry telemetry;
    private final PIDController pid;

    private double targetAngle;
    private double batteryVoltage;

    public Arm(HardwareMap hardwareMap, Telemetry telemetry) {
        armMotor = hardwareMap.get(DcMotorEx.class, "arm");
        this.telemetry = telemetry;

        pid = new PIDController(kP, kI, kD);
    }

    @Override
    public void periodic() {
        double encoder = armMotor.getCurrentPosition();
        double currentAngle = encoder * radiansPerEncoder;
        double angleError = targetAngle - currentAngle;

        pid.setPID(kP, kI, kD);

        double gravityFeedforward = kG * Math.cos(currentAngle);
        double frictionFeedforward = kS * Math.signum(angleError);
        double pidVoltage = pid.calculate(currentAngle, targetAngle);

        /// NOTE: deadband is not applied to everything. gravityFeedforward is purposefully left alone
        /// since the arm always needs to fight gravity, even while sitting still at its target;
        /// only frictionFeedforward and PID output (which should go to 0 once we're settled) are zeroed out
        if (Math.abs(angleError) < deadbandRadians) {
            frictionFeedforward = 0;
            pidVoltage = 0;
        }

        double motorVoltage = gravityFeedforward + frictionFeedforward + pidVoltage;
        double motorPower = motorVoltage / batteryVoltage;
        armMotor.setPower(motorPower);

        telemetry.addLine("ARM-----");
        telemetry.addData("A target angle", targetAngle);
        telemetry.addData("A current angle", currentAngle);
        telemetry.addData("A power", motorPower);
        telemetry.addData("A voltage", motorVoltage);
    }

    public void setTargetAngle(double targetAngle) {
        this.targetAngle = targetAngle;
    }

    public void setBatteryVoltage(double batteryVoltage) {
        this.batteryVoltage = batteryVoltage;
    }
}
