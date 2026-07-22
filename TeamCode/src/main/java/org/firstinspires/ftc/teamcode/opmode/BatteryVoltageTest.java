package org.firstinspires.ftc.teamcode.opmode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.utils.batteryFilter.BatteryVoltageFilter;

@Config
@TeleOp(name = "Battery Voltage Test")
public class BatteryVoltageTest extends LinearOpMode {
    public static double power = 0;
    @Override
    public void runOpMode() throws InterruptedException {

        telemetry = new MultipleTelemetry(FtcDashboard.getInstance().getTelemetry(), telemetry);
        telemetry.setMsTransmissionInterval(30);

        BatteryVoltageFilter batteryVoltageFilter = BatteryVoltageFilter.getInstance(hardwareMap);

        DcMotorEx shooter = hardwareMap.get(DcMotorEx.class, "shooter");

        waitForStart();

        while(opModeIsActive()) {
            batteryVoltageFilter.update();

            shooter.setPower(power);
            telemetry.addData("battery", batteryVoltageFilter.getVoltage());
            telemetry.addData("shooter velocity", shooter.getVelocity());
            telemetry.update();
        }
    }
}
