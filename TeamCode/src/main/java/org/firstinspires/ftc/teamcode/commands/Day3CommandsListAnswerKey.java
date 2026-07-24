package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.subsystems.day1Collector.CollectorChallengeAnswerKey;
import org.firstinspires.ftc.teamcode.subsystems.day1Turret.TurretAnswerKey;
import org.firstinspires.ftc.teamcode.subsystems.day3Arm.ArmAnswerKeyDay3;
import org.firstinspires.ftc.teamcode.subsystems.day3Lift.LiftAnswerKeyDay3;

public class Day3CommandsListAnswerKey {
    public static InstantCommand collectorIntake(CollectorChallengeAnswerKey collector) {
        return new InstantCommand(() -> collector.setIntakeState(CollectorChallengeAnswerKey.IntakeState.INTAKE));
    }
    public static InstantCommand collectorOff(CollectorChallengeAnswerKey collector) {
        return new InstantCommand(() -> collector.setIntakeState(CollectorChallengeAnswerKey.IntakeState.OFF));
    }
    public static InstantCommand turretToCenter(TurretAnswerKey turret) {
        return new InstantCommand(() -> {
            turret.setTurretState(TurretAnswerKey.TurretState.POINT_AT_ANGLE);
            turret.setTargetAngle(0);
        });
    }

    public static InstantCommand liftToTargetPosition(LiftAnswerKeyDay3 lift, double position) {
        return new InstantCommand(() -> lift.setTargetPos(position));
    }
    public static InstantCommand armToTargetAngle(ArmAnswerKeyDay3 arm, double angleRad) {
        return new InstantCommand(() -> arm.setTargetAngle(angleRad));
    }
    public static InstantCommand armTo90Degrees(ArmAnswerKeyDay3 arm) {
        return new InstantCommand(() -> arm.setTargetAngle(Math.toRadians(90)));
    }
}
