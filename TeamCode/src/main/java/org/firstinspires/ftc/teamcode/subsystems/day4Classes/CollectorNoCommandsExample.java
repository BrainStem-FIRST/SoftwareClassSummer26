package org.firstinspires.ftc.teamcode.subsystems.day4Classes;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

@Config
public class CollectorNoCommandsExample {
    public static double intakePower = .9;
    public static double outtakePower = -.7;
    private final DcMotorEx intakeMotor;
    private final Telemetry telemetry;

    public enum IntakeState {
        OFF, INTAKE, OUTTAKE
    }
    private IntakeState intakeState;
    private final ElapsedTime jammedTimer = new ElapsedTime();
    private boolean isJammed = false;

    public CollectorNoCommandsExample(HardwareMap hardwareMap, Telemetry telemetry) {
        // note: there's no need to store hardwareMap as instance data
        this.telemetry = telemetry;

        intakeMotor = hardwareMap.get(DcMotorEx.class, "intake");
        setIntakeState(IntakeState.OFF);

        jammedTimer.reset();
    }

    // this is badly written code
    // subsystems are supposed to hold low level code
    // intuitively if the collector is in the intake state you would expect it to be intaking
    // but now it could either be intaking or unjamming itself
    // very unclear and harder to debug
    // this jam logic should go into a command instead
    // also added extra instance data (jammedTimer)
    public void update() {
        switch(intakeState) {
            case OFF:
                intakeMotor.setPower(0);
                break;
            case INTAKE:
                if (intakeMotor.getCurrent(CurrentUnit.MILLIAMPS) > 5000 && !isJammed) {
                    isJammed = true;
                    jammedTimer.reset();
                }
                if (isJammed && jammedTimer.seconds() < 0.5)
                    intakeMotor.setPower(-0.5);
                else {
                    isJammed = false;
                    intakeMotor.setPower(intakePower);
                }
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
