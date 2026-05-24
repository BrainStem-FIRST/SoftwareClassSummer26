package org.firstinspires.ftc.teamcode.subsystems;

/*
Class Requirements
an enum called IntakeState with the states OFF, INTAKE, and EXTAKE
instance data of telemetry
instance data holding the motor, called intakeMotor (variable type is DcMotorEx)
instance data of the IntakeState enum

a constructor taking in HardwareMap and instantiating all instance data above

an update function
- has an enum switch statement that sets motor power depending on which state the collector is in
- outputs collector state and commanded motor power to telemetry

getIntakeState() - returns current intake state
setIntakeState(IntakeState intakeState) - sets the intake state of the collector to the parameter

Challenge: only call motor.setPower when the collector power changes to improve loop times by reducing hardware calls
 */

public class CollectorExample1 {
}