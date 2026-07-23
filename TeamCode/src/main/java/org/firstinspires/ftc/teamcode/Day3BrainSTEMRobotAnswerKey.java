package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.day3Arm.ArmAnswerKeyDay3;
import org.firstinspires.ftc.teamcode.subsystems.day3Lift.LiftAnswerKeyDay3;
import org.firstinspires.ftc.teamcode.utils.drivetrain.MecanumDrive;
import org.firstinspires.ftc.teamcode.utils.misc.BatteryVoltageFilter;

public class Day3BrainSTEMRobotAnswerKey {
    private final BatteryVoltageFilter batteryVoltageFilter; // I chose this to be private simply because I didn't think any classes outside bstem robot would need it
    public final MecanumDrive drive;
    public final LiftAnswerKeyDay3 lift;
    public final ArmAnswerKeyDay3 arm;
    public Day3BrainSTEMRobotAnswerKey(HardwareMap hardwareMap, Telemetry telemetry, Pose2d initialPose) {
        batteryVoltageFilter = new BatteryVoltageFilter(hardwareMap);
        drive = new MecanumDrive(hardwareMap, initialPose);
        lift = new LiftAnswerKeyDay3(hardwareMap, telemetry);
        arm = new ArmAnswerKeyDay3(hardwareMap, telemetry);
    }

    public void update() {
        double batteryVoltage = batteryVoltageFilter.getVoltage();

        /// NOTE: it is important that you first update the battery voltage for all subsystems, then call the update function
        /// if you called their update function first, they would be receiving stale battery voltage from the previous loop
        lift.setBatteryVoltage(batteryVoltage);
        arm.setBatteryVoltage(batteryVoltage);

        drive.pinpoint().update();

        lift.update();
        arm.update();
    }
}
