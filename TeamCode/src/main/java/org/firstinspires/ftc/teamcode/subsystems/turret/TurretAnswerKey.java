package org.firstinspires.ftc.teamcode.subsystems.turret;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class TurretAnswerKey {

    // you will tune and fine these values on day 2
    public static double radiansPerEncoder = 0;
    public static double kP = 0;
    public static double kI = 0;
    public static double kD = 0;
    public static double bangbangPower = 0;

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

    public TurretAnswerKey(HardwareMap hardwareMap) {
        turretMotor = hardwareMap.get(DcMotorEx.class, "turret");
        pid = new PIDController(kP, kI, kD);
        setTurretState(TurretState.OFF);
        update(); // I'm calling update() to update the currentAngle variable
    }

    public void update() {
        // updating turret angle
        int turretEncoder = turretMotor.getCurrentPosition();
        currentAngle = turretEncoder * radiansPerEncoder;

        // updating pid values from dashboard (optional)
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
                    turretMotor.setPower(bangbangPower * targetDirection);
                else {
                    turretMotor.setPower(0);
                    setTurretState(TurretState.OFF);
                }
                break;
        }
    }

    public void printTelemetry(Telemetry telemetry) {
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
            targetDirection = Math.signum(targetAngle - currentAngle);
    }

    public void setTargetAngle(double target) {
        this.targetAngle = target;
    }
}
