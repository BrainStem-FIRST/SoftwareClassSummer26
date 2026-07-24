package org.firstinspires.ftc.teamcode.utils.command;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.Subsystem;

import java.util.function.Supplier;

public class DeferredCommand extends CommandBase {

    private final Supplier<Command> supplier;
    private Command command;

    /**
     * Creates a new DeferredCommand that will construct its inner command from the given supplier
     * when it is initialized.
     *
     * @param supplier     the supplier of the command to run
     * @param requirements the subsystems required by the supplied command
     */
    public DeferredCommand(Supplier<Command> supplier, Subsystem... requirements) {
        this.supplier = supplier;

        addRequirements(requirements);
    }

    @Override
    public void initialize() {
        command = supplier.get();
        if (command != null) {
            command.initialize();
        }
    }

    @Override
    public void execute() {
        if (command != null) {
            command.execute();
        }
    }

    @Override
    public boolean isFinished() {
        return command == null || command.isFinished();
    }

    @Override
    public void end(boolean interrupted) {
        if (command != null) {
            command.end(interrupted);
        }
        command = null;
    }

}
