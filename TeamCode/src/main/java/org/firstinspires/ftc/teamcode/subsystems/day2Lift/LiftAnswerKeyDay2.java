package org.firstinspires.ftc.teamcode.subsystems.day2Lift;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class LiftAnswerKeyDay2 {
    // when first coding subsystems, you can leave these tuning values 0
    // (just remember that you have to tune them later on)
    public static double kP = 0, kI = 0, kD = 0;
    public static double kG = 0, kS = 0;
    public static double dampeningBand = 5;

    private final DcMotorEx liftMotor;
    private final PIDController pid;
    private final Telemetry telemetry;

    private double targetPos;

    public LiftAnswerKeyDay2(HardwareMap hardwareMap, Telemetry telemetry) {
        liftMotor = hardwareMap.get(DcMotorEx.class, "lift");
        this.telemetry = telemetry;

        pid = new PIDController(kP, kI, kD);
    }

    public void update() {
        int curPos = liftMotor.getCurrentPosition();
        double error = targetPos - curPos;

        pid.setPID(kP, kI, kD);

        double scaleFactor = 1;
        if (Math.abs(error) < dampeningBand)
            scaleFactor = Math.abs(error) / dampeningBand;

        /// NOTE: I am assuming that positive motor power = upwards motion
        double frictionFeedforward = Math.signum(targetPos - curPos) * kS;
        double pidPower = pid.calculate(curPos, targetPos);
        double totalPower = kG + frictionFeedforward + pidPower;

        totalPower *= scaleFactor;

        liftMotor.setPower(totalPower);
        telemetry.addData("L power", totalPower);



        telemetry.addLine("LIFT-----");
        telemetry.addData("L target pos", targetPos);
        telemetry.addData("L current pos", curPos);
    }

    public void setTargetPos(double target) {
        this.targetPos = target;
    }

}
