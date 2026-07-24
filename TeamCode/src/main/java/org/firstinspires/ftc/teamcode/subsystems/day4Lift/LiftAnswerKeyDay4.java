package org.firstinspires.ftc.teamcode.subsystems.day4Lift;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class LiftAnswerKeyDay4 extends SubsystemBase {
    // when first coding subsystems, you can leave these tuning values 0
    // (just remember that you have to tune them later on)
    public static double kP = 0, kI = 0, kD = 0;
    public static double kG = 0, kS = 0;
    public static double dampeningBand = 5;
    public static double defaultTolerance = 3;

    private final DcMotorEx liftMotor;
    private final PIDController pid;
    private final Telemetry telemetry;

    private double targetPos, curPos;
    private double batteryVoltage;

    public LiftAnswerKeyDay4(HardwareMap hardwareMap, Telemetry telemetry) {
        liftMotor = hardwareMap.get(DcMotorEx.class, "lift");
        this.telemetry = telemetry;

        pid = new PIDController(kP, kI, kD);
    }

    @Override
    public void periodic() {
        curPos = liftMotor.getCurrentPosition();
        double error = targetPos - curPos;

        pid.setPID(kP, kI, kD);

        /// NOTE: I am assuming that positive motor power = upwards motion
        double frictionVoltage = Math.signum(targetPos - curPos) * kS;
        double pidVoltage = pid.calculate(curPos, targetPos);

        ///  NOTE: dampening band is not applied to everything. the kG constant is purposefully ignored
        /// this dampening band only affects friction feedforward and PID output
        double scaleFactor = Math.min(1, Math.abs(error) / dampeningBand);
        frictionVoltage *= scaleFactor;
        pidVoltage *= scaleFactor;

        double totalVoltage = kG + frictionVoltage + pidVoltage;


        double power = totalVoltage / batteryVoltage;

        liftMotor.setPower(power);
        telemetry.addData("L voltage", totalVoltage);
        telemetry.addData("L power", power);


        telemetry.addLine("LIFT-----");
        telemetry.addData("L target pos", targetPos);
        telemetry.addData("L current pos", curPos);
    }

    public void setTargetPos(double target) {
        this.targetPos = target;
    }

    public void setBatteryVoltage(double batteryVoltage) {
        this.batteryVoltage = batteryVoltage;
    }

    public boolean withinTolerance() {
        return Math.abs(curPos - targetPos) < defaultTolerance;
    }

}
