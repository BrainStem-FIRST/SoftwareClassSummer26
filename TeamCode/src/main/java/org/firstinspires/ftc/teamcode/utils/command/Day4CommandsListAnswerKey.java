package org.firstinspires.ftc.teamcode.utils.command;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.Subsystem;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.day4Collector.Collector;
import org.firstinspires.ftc.teamcode.subsystems.day4Lift.LiftAnswerKeyDay4;

import java.util.Set;

public class Day4CommandsListAnswerKey {
    // normal implementation
    public static Command setLiftToTargetHeightBlockingCommandMethod2(LiftAnswerKeyDay4 lift) {
        SequentialCommandGroup command = new SequentialCommandGroup(
                new InstantCommand(() -> lift.setTargetPos(500)),
                new WaitUntilCommand(() -> lift.withinTolerance())
        );
        command.addRequirements(lift);
        return command;
    }

    // scarlett this is to appease you specifically hope you like it
    public static Command setLiftToTargetHeightBlockingCommandMethod1(LiftAnswerKeyDay4 lift) {
        return new CommandBuilder()
                .initialize(() -> lift.setTargetPos(500))
                .setIsFinished(() -> lift.withinTolerance())
                .requires(lift)
                .build();
    }

    // this needs to be written this way and not with CommandBuilder because it has instance data
    public static Command detectCollectorJamCommand(Collector collector) {
        return new Command() {
            private final ElapsedTime timeInOuttake = new ElapsedTime();
            @Override
            public Set<Subsystem> getRequirements() {
                return Set.of(collector);
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

            @Override
            public boolean isFinished() {
                return false;
            }
        };
    }
}
