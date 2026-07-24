package org.firstinspires.ftc.teamcode.commands;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.ConditionalCommand;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.Subsystem;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.arcrobotics.ftclib.util.InterpLUT;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.day2ColorSensor.ColorSensorWrapperAnswerKey;
import org.firstinspires.ftc.teamcode.subsystems.day4Classes.Arm;
import org.firstinspires.ftc.teamcode.subsystems.day4Classes.Collector;
import org.firstinspires.ftc.teamcode.subsystems.day4Classes.Grabber;
import org.firstinspires.ftc.teamcode.subsystems.day4Classes.Lift;
import org.firstinspires.ftc.teamcode.subsystems.day4Classes.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.day4Classes.Turret;
import org.firstinspires.ftc.teamcode.utils.command.CommandBuilder;
import org.firstinspires.ftc.teamcode.utils.command.DeferredCommand;
import org.firstinspires.ftc.teamcode.utils.drivetrain.PinpointLocalizer;

import java.util.Set;
import java.util.function.Supplier;

public class Day4CommandsListAnswerKey {
    // normal implementation
    public static Command setLiftToTargetHeightBlockingCommandMethod2(Lift lift) {
        SequentialCommandGroup command = new SequentialCommandGroup(
                new InstantCommand(() -> lift.setTargetPos(500)),
                new WaitUntilCommand(() -> lift.withinTolerance())
        );
        command.addRequirements(lift); // what does this do?
        return command;
    }

    // scarlett this is to appease you specifically hope you like it
    public static Command setLiftToTargetHeightBlockingCommandMethod1(Lift lift) {
        return new CommandBuilder()
                .initialize(() -> lift.setTargetPos(500))
                .setIsFinished(() -> lift.withinTolerance())
                .requires(lift)
                .build();
    }

    // instead of writing a full class for each command you can write an "inline declaration" like below
    // side note: this needs to be written this way and not with CommandBuilder because it has instance data
    public static Command detectCollectorJamCommand(Collector collector) {
        return new Command() {
            private final ElapsedTime timeInOuttake = new ElapsedTime();
            @Override
            public Set<Subsystem> getRequirements() {
                return Set.of(collector); // you may not want collector to be a requirement here because this command is supposed to just always run in the background
            }

            @Override
            public void initialize() {
                timeInOuttake.reset();
            }
            @Override
            public void execute() {
                if (collector.getIntakeState() != Collector.IntakeState.OUTTAKE)
                    timeInOuttake.reset();

                if (collector.getIntakeState() == Collector.IntakeState.INTAKE && collector.getMotorCurrent() > 5000) {
                    collector.setIntakeState(Collector.IntakeState.OUTTAKE);
                }
                if (collector.getIntakeState() == Collector.IntakeState.OUTTAKE && timeInOuttake.seconds() > 0.5)
                    collector.setIntakeState(Collector.IntakeState.INTAKE);
            }

            // don't actually need to write this because it is identical to default implementation
            // i just did it so it is clear that this command will never finish

            @Override
            public boolean isFinished() {
                return false;
            }
        };
    }

    public static Command aimShooterTurretToGoal(PinpointLocalizer pinpoint, Shooter shooter, Turret turret, Vector2d goal) {
        InterpLUT shooterSpeedTable = new InterpLUT();
        shooterSpeedTable.add(12, 1000);
        shooterSpeedTable.add(36, 1500);
        shooterSpeedTable.add(72, 2000);
        shooterSpeedTable.createLUT();

        return new Command() {
            @Override
            public Set<Subsystem> getRequirements() {
                return Set.of(shooter, turret);
            }

            @Override
            public void initialize() {
                shooter.setShooterState(Shooter.ShooterState.VELOCITY_CONTROL);
                turret.setTurretState(Turret.TurretState.POINT_AT_ANGLE);
            }

            @Override
            public void execute() {
                Vector2d exitPosition = pinpoint.getPose().position;
                Vector2d exitPosToGoal = goal.minus(exitPosition);

                double distToGoal = exitPosToGoal.norm();
                double angleToGoal = exitPosToGoal.angleCast().toDouble();

                shooter.setTargetVelocity(shooterSpeedTable.get(distToGoal));
                turret.setTargetAngle(angleToGoal);
            }
        };
    }



    // why is this incredibly wrong???
    public static Command aimShooterTurretToGoal(Pose2d robotPose, Shooter shooter, Turret turret, Vector2d goal) {
        InterpLUT shooterSpeedTable = new InterpLUT();
        shooterSpeedTable.add(12, 1000);
        shooterSpeedTable.add(36, 1500);
        shooterSpeedTable.add(72, 2000);
        shooterSpeedTable.createLUT();

        return new Command() {
            @Override
            public Set<Subsystem> getRequirements() {
                return Set.of(shooter, turret);
            }

            @Override
            public void initialize() {
                shooter.setShooterState(Shooter.ShooterState.VELOCITY_CONTROL);
                turret.setTurretState(Turret.TurretState.POINT_AT_ANGLE);
            }

            @Override
            public void execute() {
                Vector2d exitPosition = robotPose.position;
                Vector2d exitPosToGoal = goal.minus(exitPosition);

                double distToGoal = exitPosToGoal.norm();
                double angleToGoal = exitPosToGoal.angleCast().toDouble();

                shooter.setTargetVelocity(shooterSpeedTable.get(distToGoal));
                turret.setTargetAngle(angleToGoal);
            }
        };
    }

    // the Supplier class can replace pinpoint
    // explain <> and what that means
    public static Command aimShooterTurretToGoal(Supplier<Pose2d> robotPoseSupplier, Shooter shooter, Turret turret, Vector2d goal) {
        InterpLUT shooterSpeedTable = new InterpLUT();
        shooterSpeedTable.add(12, 1000);
        shooterSpeedTable.add(36, 1500);
        shooterSpeedTable.add(72, 2000);
        shooterSpeedTable.createLUT();

        return new Command() {
            @Override
            public Set<Subsystem> getRequirements() {
                return Set.of(shooter, turret);
            }

            @Override
            public void initialize() {
                shooter.setShooterState(Shooter.ShooterState.VELOCITY_CONTROL);
                turret.setTurretState(Turret.TurretState.POINT_AT_ANGLE);
            }

            @Override
            public void execute() {
                Vector2d exitPosition = robotPoseSupplier.get().position;
                Vector2d exitPosToGoal = goal.minus(exitPosition);

                double distToGoal = exitPosToGoal.norm();
                double angleToGoal = exitPosToGoal.angleCast().toDouble();

                shooter.setTargetVelocity(shooterSpeedTable.get(distToGoal));
                turret.setTargetAngle(angleToGoal);
            }
        };
    }


    /*
    implement this:
    make the arm point downwards
    open the grabber (since it is a servo then wait 0.3 seconds)
    move the lift down
    wait until the lift is below 50
    close the grabber (wait 0.3 seconds)
    move the lift up
    when the lift is past 300 then rotate the arm
    when the lift reaches its destination, open the grabber
    */




    // another example
    public static Command grabBlock(Lift lift, Arm arm, Grabber grabber) {
        return null;
    }
    public static Command depositBlockHigh(Lift lift, Arm arm, Grabber grabber) {
        return null;
    }
    public static Command depositBlockBehind(Lift lift, Arm arm, Grabber grabber) {
        return null;
    }

    // different ways to put it together
    public static Command grabAndDepositBlock1(ColorSensorWrapperAnswerKey colorSensor, Lift lift, Arm arm, Grabber grabber) {
        return new SequentialCommandGroup(
                grabBlock(lift, arm, grabber),
                new WaitCommand(500),
                new DeferredCommand(
                        () -> {
                            if (colorSensor.seesColorInRange())
                                return depositBlockHigh(lift, arm, grabber);
                            return depositBlockBehind(lift, arm, grabber);
                        }
                )
        );
    }
    public static Command grabAndDepositBlock2(ColorSensorWrapperAnswerKey colorSensor, Lift lift, Arm arm, Grabber grabber) {
        return new SequentialCommandGroup(
                grabBlock(lift, arm, grabber),
                new WaitCommand(500),
                new ConditionalCommand(
                        depositBlockHigh(lift, arm, grabber),
                        depositBlockBehind(lift, arm, grabber),
                        () -> colorSensor.seesColorInRange()
                )
        );
    }
}
