package org.firstinspires.ftc.teamcode.subsystems.day4Classes;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class Turret extends SubsystemBase {

    // you will tune and find these values on day 2
    public static double radiansPerEncoder = 0;
    public static double kP = 0;
    public static double kI = 0;
    public static double kD = 0;
    public static double bangBangPower = 0;

    private final DcMotorEx turretMotor;
    private double currentAngle;
    private final PIDController pid;
    private double targetAngle;
    private double targetDirection;

    public enum TurretState {
        OFF,
        POINT_AT_ANGLE,
        SWING_PAST_ANGLE
    }
    private TurretState turretState;
    private final Telemetry telemetry;

    public Turret(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        turretMotor = hardwareMap.get(DcMotorEx.class, "turret");
        resetTurretEncoder();
        pid = new PIDController(kP, kI, kD);
        setTurretState(TurretState.OFF);
    }

    @Override
    public void periodic() {
        // updating turret angle
        // with bulk caching off, the getCurrentPosition function does a hardware call every single time
        // to minimize loop times, save the value once at the start of the update function then reuse it whenever you want
        double turretEncoder = turretMotor.getCurrentPosition();
        // note how currentAngle IS instance data and turretEncoder IS NOT because there is no need to make both instance data
        // it is good practice to minimize the amount of instance data your classes have because instance data is much harder to read vs local variables
        currentAngle = turretEncoder * radiansPerEncoder;

        // updating pid values from dashboard (optional)
        // previously you might have wondered why some pid values require you to restart the op mode to update, and some don't
        // when you update pid values in the update function (which is called every loop), you won't need to restart the op mode to update the values
        // if you didn't have this line of code though, you would have to restart the op mode
        pid.setPID(kP, kI, kD);

        switch(turretState) {
            case OFF:
                turretMotor.setPower(0);
                break;
            case POINT_AT_ANGLE:
                double power = pid.calculate(currentAngle, targetAngle);
                turretMotor.setPower(power);
                break;

            // this is the challenge portion
            case SWING_PAST_ANGLE:
                if(Math.signum(targetAngle - currentAngle) == targetDirection)
                    turretMotor.setPower(bangBangPower * targetDirection);
                else {
                    turretMotor.setPower(0);
                    setTurretState(TurretState.OFF);
                }
                break;
        }

        telemetry.addLine("TURRET----------");
        telemetry.addData("T state", turretState);
        telemetry.addData("T power", turretMotor.getPower());
        telemetry.addData("T target direction", targetDirection);
        telemetry.addData("T target angle", targetAngle);
        telemetry.addData("T current angle", currentAngle);
    }

    public TurretState getTurretState() {
        return turretState;
    }
    public void setTurretState(TurretState turretState) {
        this.turretState = turretState;

        // this is the challenge portion
        if(turretState == TurretState.SWING_PAST_ANGLE)
            targetDirection = Math.signum(targetAngle - getCurrentAngle());
    }

    public double getCurrentAngle() {
        return currentAngle;
    }
    public void setTargetAngle(double target) {
        this.targetAngle = target;
    }

    public void resetTurretEncoder() {
        turretMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        currentAngle = 0; // reset the currentAngle instance data or else it will be outdated
        turretMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); // need to remember to set mode back to RUN_WITHOUT_ENCODER or else it will stay in STOP_AND_RESET_ENCODER mode
    }

}
