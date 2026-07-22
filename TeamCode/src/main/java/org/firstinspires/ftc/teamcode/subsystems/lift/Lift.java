    package org.firstinspires.ftc.teamcode.subsystems.lift;
    import com.acmerobotics.dashboard.config.Config;
    import com.arcrobotics.ftclib.controller.PIDController;
    import com.arcrobotics.ftclib.util.InterpLUT;
    import com.qualcomm.robotcore.hardware.DcMotorEx;
    import com.qualcomm.robotcore.hardware.HardwareMap;

    import org.firstinspires.ftc.robotcore.external.Telemetry;

    @Config
    public class Lift {

        // static variables
        public static double kG = 0.0;
        public static double kS = 0.0;
        public static double kP = 0.0;
        public static double kI = 0.0;
        public static double kD = 0.0;
        public static double[] interplutInput =
                new double[] {0, 50, 100, 150, 200};
        public static double[] interplutOutput =
                new double[] {0.3, 0.4, 0.3, 0.2, 0.1};

        // instance data
        private final PIDController pid;
        private final Telemetry telemetry;
        private final DcMotorEx liftMotor;
        private double targetPosition;
        private double currentPosition;
        private InterpLUT interpLUT;

        public Lift(HardwareMap hardwareMap, Telemetry telemetry) {
            this.telemetry = telemetry;
            liftMotor = hardwareMap.get(DcMotorEx.class, "lift");
            pid = new PIDController(kP, kI, kD);
            interpLUT = new InterpLUT();
            for (int i = 0; i < interplutInput.length; i++) {
                interpLUT.add(
                        interplutInput[i],
                        interplutOutput[i]
                );
            }
            interpLUT.createLUT();
        }

        public void update() {
            pid.setPID(kP, kI, kD);
            double liftEncoder = liftMotor.getCurrentPosition();
            currentPosition = liftEncoder;
            double gravityPower = interpLUT.get(currentPosition);
            double frictionPower =
                    Math.signum(targetPosition - currentPosition) * kS;
            double pidPower =
                    pid.calculate(currentPosition, targetPosition);
            double totalPower =
                    pidPower + gravityPower + frictionPower;

            liftMotor.setPower(totalPower);
            telemetry.addLine("LIFT-------");
            telemetry.addData("Current position", currentPosition);
            telemetry.addData("Target position", targetPosition);
            telemetry.addData("PID power", pidPower);
            telemetry.addData("Total power", totalPower);
        }

        public void setTargetPosition(double target) {
            this.targetPosition = target;
        }

    }
