package org.firstinspires.ftc.teamcode.subsystems.Lift;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.util.InterpLUT;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class LiftChallengeAnswerKey {
    // when first coding subsystems, you can leave these tuning values 0
    // (just remember that you have to tune them later on)
    public static double kP = 0, kI = 0, kD = 0;
    public static double[] kGLookupInput = new double[] { 0, 50, 100, 150, 200 };
    public static double[] kGLookupOutput = new double[] { .3, .4, .3, .2, .1 };
     private final DcMotorEx liftMotor;
    private final PIDController pid;
    private final InterpLUT kGLookup;
    private final Telemetry telemetry;

    private double targetPos;

    public LiftChallengeAnswerKey(HardwareMap hardwareMap, Telemetry telemetry) {
        liftMotor = hardwareMap.get(DcMotorEx.class, "lift");
        this.telemetry = telemetry;

        pid = new PIDController(kP, kI, kD);

        // creating kG lookup table to account for varying string tension in lift
        kGLookup = new InterpLUT();
        for(int i = 0; i < kGLookupInput.length; i++)
            kGLookup.add(kGLookupInput[i], kGLookupOutput[i]);
        kGLookup.createLUT();
    }

    public void update() {
        int curPos = liftMotor.getCurrentPosition();
        pid.setPID(kP, kI, kD);

        /// NOTE: I am assuming that positive motor power = upwards motion
        double lookupInput = Range.clip(curPos, kGLookupInput[0], kGLookupInput[kGLookupInput.length - 1]);
        double feedforward = kGLookup.get(lookupInput);
        double pidPower = pid.calculate(curPos, targetPos);
        double totalPower = Range.clip(feedforward + pidPower, -1, 1); // you don't have to clip this
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
