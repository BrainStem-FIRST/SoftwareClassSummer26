package org.firstinspires.ftc.teamcode.opmode;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.DetectCollectorJamCommand;
import org.firstinspires.ftc.teamcode.subsystems.day4Classes.Collector;


@TeleOp(name="CollectorCommandTele")
public class CollectorCommandTele extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        CommandScheduler.getInstance().reset();

        Collector collector = new Collector(hardwareMap, telemetry);

        CommandScheduler.getInstance().schedule(new DetectCollectorJamCommand(collector));

        waitForStart();

        while(opModeIsActive()) {

            // any logic that updates subsystem states need to go before calling CommandScheduler.getInstance().run()
            // i.e gamepad logic
            if (gamepad1.aWasPressed())
                collector.setIntakeState(Collector.IntakeState.INTAKE);
            if (gamepad1.bWasPressed())
                collector.setIntakeState(Collector.IntakeState.OFF);

            // this calls the execute function on all active commands
            // also calls isFinished and if the command is finished, removes it from the list of active commands
            // also calls the collector.periodic function because collector extends SubsystemBase so you don't have to do it
            CommandScheduler.getInstance().run();

            telemetry.update();
        }
    }
}
