package org.firstinspires.ftc.teamcode.subsystems.collector;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class CollectorBasicAnswerKey {
    public static double intakePower = .9;
    public static double outtakePower = -.7;
    private final DcMotorEx intakeMotor;

    public enum IntakeState {
        OFF, INTAKE, OUTTAKE
    }
    private IntakeState intakeState;

    public CollectorBasicAnswerKey(HardwareMap hardwareMap) {
        // note: there's no need to store hardwareMap as instance data

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
    }

    public void printTelemetry(Telemetry telemetry) {
        // note: I put the "C" prefix on my collector telemetry because FTC Dashboard displays telemetry alphabetically
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
