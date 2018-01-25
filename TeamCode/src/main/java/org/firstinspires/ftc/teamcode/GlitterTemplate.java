package org.firstinspires.ftc.teamcode;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by Hunter on 7/30/2017.
 */
@Autonomous(name="Glitter Template",group="LinearOpMode")
public class GlitterTemplate extends LinearOpMode {
    GlitterHardware r = new GlitterHardware();

    AutonomousTextOption allianceColor = new AutonomousTextOption("Alliance Color", "blue", new String[] {"Blue", "Red"});
    AutonomousTextOption strategy = new AutonomousTextOption("Strategy", "Do-It-All-Ramp", new String [] {"Do-It-All-Ramp", "Beacon-Ramp","Shooter-Angled","Shooter-Ramp","Blitz-Ramp"});
    AutonomousIntOption waitStart = new AutonomousIntOption("Wait at Start", 0, 0, 20);
    AutonomousBooleanOption park = new AutonomousBooleanOption("Park", true);

    AutonomousOption [] autoOptions = {allianceColor,strategy,waitStart,park};
    int currentOption = 0;

    public void initializeRobot(){
        /* Insert initialization code here */
        //encoderDrive(.35,24,24); // Go forward 2 ft.
    }

    public void runOpMode() throws InterruptedException{
        r.init(hardwareMap);
        initializeRobot();
        waitForStart();

        telemetry.addData("Status", "Resetting Encoders");
        telemetry.update();

        r.leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        r.rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        idle();
        encoderDrive(.35,12,12); // Go forward 2 ft.
        /* Insert GlitterWrite code here */
    }

    /////////////////////////////////////////
    //            REQUIRED CODE            //
    //      Do not edit past this point    //
    /////////////////////////////////////////

    public void encoderDrive(double speed, double leftInches, double rightInches) {
        int newLeftTarget;
        int newRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = r.leftMotor.getCurrentPosition() + (int)(leftInches * r.ticksPerInch);
            newRightTarget = r.rightMotor.getCurrentPosition() + (int)(rightInches * r.ticksPerInch);
            r.leftMotor.setTargetPosition(newLeftTarget);
            r.rightMotor.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            r.leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            r.rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            r.leftMotor.setPower(Math.abs(speed));
            r.rightMotor.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            while (opModeIsActive() && (r.leftMotor.isBusy() && r.rightMotor.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d", r.leftMotor.getCurrentPosition(), r.rightMotor.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            r.leftMotor.setPower(0);
            r.rightMotor.setPower(0);

            r.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            r.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            sleep(250);
        }
    }

    /////////////////////////////////////////
    //            REQUIRED CODE            //
    //         Smart Menu Functions        //
    /////////////////////////////////////////
    public void selectOptions() {
        boolean aPressed = false;
        boolean yPressed = false;
        boolean bPressed = false;
        boolean xPressed = false;
        while (currentOption < autoOptions.length && !opModeIsActive()) {
            showOptions();
            if (gamepad1.a && !aPressed) {
                currentOption = currentOption + 1;
                aPressed = true;
            } else {
                aPressed = gamepad1.a;
            }
            if (gamepad1.y && !yPressed) {
                currentOption = currentOption - 1;
                yPressed = true;
            } else {
                yPressed = gamepad1.y;
            }
            if (gamepad1.b && !bPressed) {
                autoOptions[currentOption].nextValue();
                bPressed = true;
            } else {
                bPressed = gamepad1.b;
            }
            if (gamepad1.x && !xPressed) {
                autoOptions[currentOption].previousValue();
                xPressed = true;
            } else {
                xPressed = gamepad1.x;
            }
        }
    }

    private void showOptions() {
        String str = "";
        switch (autoOptions[currentOption].optionType) {
            case STRING:
                str = ((AutonomousTextOption) autoOptions[currentOption]).getValue();
                break;
            case INT:
                str = Integer.toString(((AutonomousIntOption) autoOptions[currentOption]).getValue());
                break;
            case BOOLEAN:
                str = String.valueOf(((AutonomousBooleanOption) autoOptions[currentOption]).getValue());
                break;
        }

        if(currentOption == autoOptions.length-1){
            telemetry.addLine("READY!");

        }
        else{
            telemetry.addLine(autoOptions[currentOption].name);
            telemetry.addLine("Current Option: " + str);
            telemetry.update();
        }
        telemetry.update();
    }
}
