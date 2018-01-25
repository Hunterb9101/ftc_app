package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;
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
    public Servo leftTopGrabber = null; // Left Grabber on block mechanism
    public Servo rightTopGrabber = null; // Right Grabber on block mechanism
    public Servo leftBottomGrabber = null; // Left Grabber on block mechanism
    public Servo rightBottomGrabber = null; // Right Grabber on block mechanism

    public ColorSensor colorSensor = null;
    //static final double ticksPerInch = (1237) / (8 * 4 * Math.PI); Old TPI
    static final double ticksPerInch =  3*(1237) / (4 * Math.PI * 12); // (Ticks on Motor * Gear Reduction) / (Wheel Diameter * Pi)
    static final double inchesPerDegrees = 2 * 19.2/12;
    int cnt = 0;
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
        colorSensor = hwMap.get(ColorSensor.class, "jewelcolorsensor");

        jewel = hwMap.servo.get("jewel");
        leftTopGrabber = hwMap.servo.get("leftGrabber");
        rightTopGrabber = hwMap.servo.get("rightGrabber");
        leftBottomGrabber = hwMap.servo.get("leftBottomGrabber");
        rightBottomGrabber = hwMap.servo.get("rightBottomGrabber");

        /*
        leftMotor.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        rightMotor.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors
        */
        leftMotor.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        rightMotor.setDirection(DcMotor.Direction.FORWARD);// Set to FORWARD if using AndyMark motors
        lift.setDirection(DcMotor.Direction.FORWARD);


        // Set all motors to zero power
        leftMotor.setPower(0);
        rightMotor.setPower(0);
        lift.setPower(0);
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

    public void openGrabber() throws InterruptedException{
        if(cnt == 1){
            leftTopGrabber.setPosition(.9);
            rightTopGrabber.setPosition(.1);
            leftBottomGrabber.setPosition(.9);
            rightBottomGrabber.setPosition(.1);
        }
        else{
            leftTopGrabber.setPosition(.6);
            rightTopGrabber.setPosition(.4);
            leftBottomGrabber.setPosition(.6);
            rightBottomGrabber.setPosition(.4);
        }
        cnt++;
        Thread.sleep(250);
    }

    public void closeGrabber(){
        leftTopGrabber.setPosition(.45);
        rightTopGrabber.setPosition(.55);
        leftBottomGrabber.setPosition(.45);
        rightBottomGrabber.setPosition(.55);
        cnt = 0;
    }
}

