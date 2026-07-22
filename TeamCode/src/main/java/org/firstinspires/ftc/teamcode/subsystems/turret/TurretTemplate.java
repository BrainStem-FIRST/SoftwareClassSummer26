package org.firstinspires.ftc.teamcode.subsystems.turret;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class TurretTemplate {

    // declare your static variables below
    public static double radiansPerEncoder = 0.0;
    public static double kP = 0.0;
    public static double kI = 0.0;
    public static double kD = 0.0;
    public static double bangBangPower = 0.0;

    // declare your instance data below
    private final PIDController pid;
    private final Telemetry telemetry;
    private final DcMotorEx turretMotor;
    private double targetAngle;
    private double currentAngle;
    private double targetDirection;
    private TurretState turretState;
    public enum TurretState {OFF, POINT_AT_ANGLE, SWING_PAST_ANGLE}
    public TurretTemplate(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;
        turretMotor=hardwareMap.get(DcMotorEx.class,"turret");
        pid = new PIDController(kP, kI, kD);
        setTurretState(TurretState.OFF);
        resetTurretEncoder();
    }

    public void update() {
        // calculate the current turret angle below
        currentAngle = turretMotor.getCurrentPosition() * radiansPerEncoder;
        //pid values in dashboard
        pid.setPID(kP, kI, kD);
        // switch statement goes here
        switch(turretState) {
            case OFF:
                turretMotor.setPower(0);
                break;
            case POINT_AT_ANGLE:
                turretMotor.setPower(pid.calculate(currentAngle, targetAngle));
                break;
            case SWING_PAST_ANGLE:
                if (targetDirection * currentAngle < targetDirection * targetAngle) {
                    turretMotor.setPower(bangBangPower * targetDirection);
                } else {
                    turretState = TurretState.OFF;
                    turretMotor.setPower(0);
                }
                break;
        }
        telemetry.addLine("TURRET----------");
        telemetry.addData("Turret state", turretState);
        telemetry.addData("Target Angle", targetAngle);
        telemetry.addData("Current Angle", currentAngle);
        telemetry.addData("Target Direction", targetDirection);
        telemetry.addData("Turret power", turretMotor.getPower());

    }

    // finish these functions below
    public TurretState getTurretState() {return turretState;
    }
    public void setTurretState(TurretState turretState) {this.turretState = turretState;
        if (turretState == TurretState.SWING_PAST_ANGLE) {
            if (targetAngle > currentAngle) {
                targetDirection = 1.0;
            }
            else {
                targetDirection = -1.0;
            }
        }
    }
    public double getCurrentAngle() {return currentAngle;
    }
    public void setTargetAngle(double target) {this.targetAngle=target;
    }
   public void resetTurretEncoder() {turretMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
       currentAngle = 0;
       turretMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
}
