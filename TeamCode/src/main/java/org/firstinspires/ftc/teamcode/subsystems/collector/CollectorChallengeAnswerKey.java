package org.firstinspires.ftc.teamcode.subsystems.collector;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class CollectorChallengeAnswerKey {
    public static double intakePower = .9;
    public static double outtakePower = -.7;
    private final DcMotorEx intakeMotor;

    public enum IntakeState {
        OFF, INTAKE, OUTTAKE
    }
    private IntakeState intakeState;

    public CollectorChallengeAnswerKey(HardwareMap hardwareMap) {
        // note: there's no need to store hardwareMap as instance data

        intakeMotor = hardwareMap.get(DcMotorEx.class, "intake");
        setIntakeState(IntakeState.OFF);
    }

    public void update() {}

    public void printTelemetry(Telemetry telemetry) {
        telemetry.addLine("---INTAKE---");
        telemetry.addData("I state", intakeState);
        telemetry.addData("I power", intakeMotor.getPower());
        // YOU SHOULD NOT HAVE A "telemetry.update();" HERE
    }

    public IntakeState getIntakeState() {
        return intakeState;
    }
    public void setIntakeState(IntakeState intakeState) {
        if(this.intakeState == intakeState)
            return;

        this.intakeState = intakeState;
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
}
