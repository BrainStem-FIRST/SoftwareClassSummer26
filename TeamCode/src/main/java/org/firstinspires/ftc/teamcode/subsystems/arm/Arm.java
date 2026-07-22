package org.firstinspires.ftc.teamcode.subsystems.arm;
import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.shooter.ShooterTemplate;
@Config
public class Arm {

    // static variables
    public static double radiansPerEncoder = 0;
    public static double kS = 0.0;
    public static double kG = 0.0;
    public static double kP = 0.0;
    public static double kI = 0.0;
    public static double kD = 0.0;

    // instance data
    private final PIDController pid;
    private final Telemetry telemetry;
    private final DcMotorEx armMotor;
    private double targetDirection;
    private double currentAngle;
    private double targetAngle;

    public Arm(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;
        armMotor = hardwareMap.get(DcMotorEx.class, "arm");
        resetArmEncoder();
        pid = new PIDController(kP, kI, kD);
    }

    public void update() {
        pid.setPID(kP, kI, kD);
        double armEncoder = armMotor.getCurrentPosition();
        currentAngle = armEncoder * radiansPerEncoder;
        targetDirection = Math.signum(targetAngle - currentAngle);
        double pidPower =
                pid.calculate(currentAngle, targetAngle);
        double gravityPower =
                Math.cos(currentAngle) * kG;
        double frictionPower =
                targetDirection * kS;
        double totalPower =
                pidPower + gravityPower + frictionPower;

        armMotor.setPower(totalPower);
        telemetry.addLine("ARM-------");
        telemetry.addData("Current angle", currentAngle);
        telemetry.addData("Target angle", targetAngle);
        telemetry.addData("Target direction", targetDirection);
        telemetry.addData("PID power", pidPower);
        telemetry.addData("Gravity power", gravityPower);
        telemetry.addData("Friction power", frictionPower);
        telemetry.addData("Total power", totalPower);
    }

    public double getCurrentAngle() {
        return currentAngle;
    }
    public void setTargetAngle(double target) {
        this.targetAngle = target;
    }

    public void resetArmEncoder() {
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        currentAngle = 0; // reset the currentAngle instance data or else it will be outdated
        armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); // need to remember to set mode back to RUN_WITHOUT_ENCODER or else it will stay in STOP_AND_RESET_ENCODER mode
    }
}
