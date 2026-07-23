package org.firstinspires.ftc.teamcode.subsystems.day1Collector;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class CollectorBasicAnswerKey {
    public static double intakePower = .9;
    public static double outtakePower = -.7;
    private final DcMotorEx intakeMotor;
    private final Telemetry telemetry;

    public enum IntakeState {
        OFF, INTAKE, OUTTAKE
    }
    private IntakeState intakeState;

    public CollectorBasicAnswerKey(HardwareMap hardwareMap, Telemetry telemetry) {
        // note: there's no need to store hardwareMap as instance data
        this.telemetry = telemetry;

        intakeMotor = hardwareMap.get(DcMotorEx.class, "intake");
        setIntakeState(IntakeState.OFF);
    }

    public void update() {
        switch(intakeState) {
            case OFF:
                intakeMotor.setPower(0);
                break;
            case INTAKE:
                intakeMotor.setPower(intakePower);
                break;
            case OUTTAKE:
                intakeMotor.setPower(outtakePower);
                break;
        }

        // note: FTC Dashboard displays telemetry alphabetically, so if you want your collector telemetry to group together keep that in mind
        // you don't need to copy this format, but you should find some way to keep your telemetry organized
        telemetry.addLine("---COLLECTOR---");
        telemetry.addData("C state", intakeState);
        telemetry.addData("C power", intakeMotor.getPower());
        // YOU SHOULD NOT HAVE A "telemetry.update();" HERE
    }

    public IntakeState getIntakeState() {
        return intakeState;
    }
    public void setIntakeState(IntakeState intakeState) {
        this.intakeState = intakeState;
    }
}
