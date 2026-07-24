package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.Subsystem;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.day4Classes.Collector;

import java.util.Set;

// this is where the jam logic SHOULD live
public class DetectCollectorJamCommand implements Command {
    private final Collector collector;
    private final ElapsedTime timeInOuttake = new ElapsedTime();

    public DetectCollectorJamCommand(Collector collector) {
        this.collector = collector;
    }

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
}
