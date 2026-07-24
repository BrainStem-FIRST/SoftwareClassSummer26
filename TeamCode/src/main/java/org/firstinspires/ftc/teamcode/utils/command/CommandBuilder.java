package org.firstinspires.ftc.teamcode.utils.command;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.FunctionalCommand;
import com.arcrobotics.ftclib.command.Subsystem;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class CommandBuilder {

    private Runnable onInitialize = () -> {};
    private Runnable execute = () -> {};
    private BooleanSupplier isFinished = () -> false;
    private Consumer<Boolean> onEnd = interrupted -> {};
    private final Set<Subsystem> requirements = new HashSet<>();

    /**
     * @param onInitialize function for code that runs once when the command starts.
     */
    public CommandBuilder initialize(Runnable onInitialize) {
        this.onInitialize = onInitialize;
        return this;
    }

    /**
     * @param onExecute Code that runs every scheduler loop.
     */
    public CommandBuilder execute(Runnable onExecute) {
        this.execute = onExecute;
        return this;
    }

    /**
     * @param isFinished returns true when the command should finish.
     */
    public CommandBuilder setIsFinished(BooleanSupplier isFinished) {
        this.isFinished = isFinished;
        return this;
    }

    /**
     * What to run when the command ends
     */
    public CommandBuilder end(Consumer<Boolean> onEnd) {
        this.onEnd = onEnd;
        return this;
    }

    /**
     * Adds subsystem requirements.
     */
    public CommandBuilder requires(Subsystem... requirements) {
        this.requirements.addAll(Arrays.asList(requirements));
        return this;
    }

    /**
     * Builds the FTCLib command.
     */
    public Command build() {
        return new FunctionalCommand(
                onInitialize,
                execute,
                onEnd,
                isFinished,
                requirements.toArray(new Subsystem[0])
        );
    }
}