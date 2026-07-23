package org.firstinspires.ftc.teamcode.subsystems.day2Arm;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class ArmAnswerKeyDay2 {
    // when first coding subsystems, you can leave these tuning values 0
    // (just remember that you have to tune them later on)
    public static double kG = 0, kS = 0;
    public static double kP = 0, kI = 0, kD = 0;
    public static double radiansPerEncoder = 0;

    private final DcMotorEx armMotor;
    private final Telemetry telemetry;
    private final PIDController pid;

    private double targetAngle;

    public ArmAnswerKeyDay2(HardwareMap hardwareMap, Telemetry telemetry) {
        armMotor = hardwareMap.get(DcMotorEx.class, "arm");
        this.telemetry = telemetry;

        pid = new PIDController(kP, kI, kD);
    }

    public void update() {
        double encoder = armMotor.getCurrentPosition();
        double currentAngle = encoder * radiansPerEncoder;

        pid.setPID(kP, kI, kD);

        double gravityFeedforward = kG * Math.cos(currentAngle);
        double frictionFeedforward = kS * Math.signum(targetAngle - currentAngle);

        double pidPower = pid.calculate(currentAngle, targetAngle);

        double motorPower = gravityFeedforward + frictionFeedforward + pidPower;
        armMotor.setPower(motorPower);

        telemetry.addLine("ARM-----");
        telemetry.addData("A target angle", targetAngle);
        telemetry.addData("A current angle", currentAngle);
        telemetry.addData("A power", motorPower);
    }

    public void setTargetAngle(double targetAngle) {
        this.targetAngle = targetAngle;
    }
}
