package org.firstinspires.ftc.teamcode.subsystems.day4Classes;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class LimelightCamera {
    private final Telemetry telemetry;
    private final Limelight3A limelight;
    private LLResult latestResult;

    public LimelightCamera(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
    }

    public void update() {
        latestResult = limelight.getLatestResult();
        telemetry.addData("tx", getTx());
        telemetry.addData("seesBlob", seesBlob());
    }

    public double getTx() {
        return latestResult.getTx();
    }
    public boolean seesBlob() {
        return latestResult.isValid();
    }
}
