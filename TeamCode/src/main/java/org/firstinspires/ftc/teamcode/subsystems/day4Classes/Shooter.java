package org.firstinspires.ftc.teamcode.subsystems.day4Classes;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class Shooter extends SubsystemBase {
    public static double kV = 0, kS = 0;
    public static double kP = 0, kI = 0, kD = 0;

    private final Telemetry telemetry;
    private final DcMotorEx shooterMotor;
    private final PIDController pid;

    public enum ShooterState {
        OFF, CONSTANT_POWER, VELOCITY_CONTROL
    }
    private ShooterState shooterState;

    private double targetPower;
    private double targetVelocity;

    public Shooter(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;
        shooterMotor = hardwareMap.get(DcMotorEx.class, "shooter"); // the string name will vary based on your hardware map config
        pid = new PIDController(kP, kI, kD);
        setShooterState(ShooterState.OFF);
    }

    @Override
    public void periodic() {
        double currentVelocity = shooterMotor.getVelocity();
        pid.setPID(kP, kI, kD);

        switch(shooterState) {
            case OFF:
                shooterMotor.setPower(0);
                break;
            case CONSTANT_POWER:
                shooterMotor.setPower(targetPower);
                break;
            case VELOCITY_CONTROL:
                shooterMotor.setPower(calculatePower(currentVelocity, targetVelocity));
                break;
        }

        telemetry.addLine("SHOOTER-----");
        telemetry.addData("S state", shooterState);
        telemetry.addData("S target velocity", targetVelocity);
        telemetry.addData("S current velocity", currentVelocity);
        telemetry.addData("S motor power", shooterMotor.getPower());
    }

    // TODO: use your equations to fill out this function below
    private double calculatePower(double currentVelocity, double targetVelocity) {
        double velocityPower = kV * targetVelocity;
        double pidPower = pid.calculate(currentVelocity, targetVelocity);
        return velocityPower + pidPower + kS;
    }

    public void setShooterState(ShooterState shooterState) {
        if(this.shooterState != shooterState)
            pid.reset();
        this.shooterState = shooterState;
    }
    public ShooterState getShooterState() {
        return shooterState;
    }

    public void setTargetVelocity(double target) {
        this.targetVelocity = target;
    }
    public void setTargetPower(double power) {
        this.targetPower = power;
    }
}
