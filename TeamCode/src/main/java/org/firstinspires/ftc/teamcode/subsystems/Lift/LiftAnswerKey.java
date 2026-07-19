package org.firstinspires.ftc.teamcode.subsystems.Lift;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class LiftAnswerKey {
    // when first coding subsystems, you can leave these tuning values 0
    // (just remember that you have to tune them later on)
    public static double kP = 0, kI = 0, kD = 0;
    public static double kG = 0;

    private final DcMotorEx liftMotor;
    private final PIDController pid;
    private final Telemetry telemetry;

    private double targetPos;

    public LiftAnswerKey(HardwareMap hardwareMap, Telemetry telemetry) {
        liftMotor = hardwareMap.get(DcMotorEx.class, "lift");
        this.telemetry = telemetry;

        pid = new PIDController(kP, kI, kD);
    }

    public void update() {
        int curPos = liftMotor.getCurrentPosition();
        pid.setPID(kP, kI, kD);

        /// NOTE: I am assuming that positive motor power = upwards motion
        double pidPower = pid.calculate(curPos, targetPos);
        double totalPower = Range.clip(kG + pidPower, -1, 1); // you don't need to clip this
        liftMotor.setPower(totalPower);


        telemetry.addLine("LIFT-----");
        telemetry.addData("L target pos", targetPos);
        telemetry.addData("L current pos", curPos);
        telemetry.addData("L power", totalPower);
    }

    public void setTargetPos(double target) {
        this.targetPos = target;
    }

}
