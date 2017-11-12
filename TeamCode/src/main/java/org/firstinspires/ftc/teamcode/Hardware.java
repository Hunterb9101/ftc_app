package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Hardware {
    /* Public OpMode members. */
    public DcMotor leftMotor   = null; // These are the drive wheels
    public DcMotor rightMotor  = null;
    public DcMotor lift = null; // For the lift mechanism

    public Servo jewel = null; // For the jewel knock-off mechanism
    public Servo leftGrabber = null; // Left Grabber on block mechanism
    public Servo rightGrabber = null; // Right Grabber on block mechanism

    static final double ticksPerInch = (1237) / (4.0 * Math.PI); // (Ticks on Motor * Gear Reduction) / (Wheel Diameter * Pi)

    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    public Hardware(){}

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        leftMotor   = hwMap.dcMotor.get("leftMotor");
        rightMotor  = hwMap.dcMotor.get("rightMotor");
        lift = hwMap.dcMotor.get("lift");

        jewel = hwMap.servo.get("jewel");
        leftGrabber = hwMap.servo.get("leftGrabber");
        rightGrabber = hwMap.servo.get("rightGrabber");

        leftMotor.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        rightMotor.setDirection(DcMotor.Direction.FORWARD);// Set to FORWARD if using AndyMark motors
        lift.setDirection(DcMotor.Direction.FORWARD);


        // Set all motors to zero power
        leftMotor.setPower(0);
        rightMotor.setPower(0);
        lift.setPower(0);

        //Grabber can pick up block w/ following settings
        //leftGrabber.setPosition(.4);
        //rightGrabber.setPosition(.6);
        leftGrabber.setPosition(.85);
        rightGrabber.setPosition(.15);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    /***
     *
     * waitForTick implements a periodic delay. However, this acts like a metronome with a regular
     * periodic tick.  This is used to compensate for varying processing times for each cycle.
     * The function looks at the elapsed cycle time, and sleeps for the remaining time interval.
     *
     * @param periodMs  Length of wait cycle in mSec.
     * @throws InterruptedException
     */
    public void waitForTick(long periodMs) throws InterruptedException {
        long  remaining = periodMs - (long)period.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0)
            Thread.sleep(remaining);

        period.reset(); // Reset the cycle clock for the next pass.

    }
}

