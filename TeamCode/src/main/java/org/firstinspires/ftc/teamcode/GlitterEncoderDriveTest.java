package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by Hunter on 7/30/2017.
 */
@Autonomous(name="Glitter TEST",group="LinearOpMode")
public class GlitterEncoderDriveTest extends LinearOpMode {
    Hardware r = new Hardware();

    AutonomousTextOption allianceColor = new AutonomousTextOption("Alliance Color", "blue", new String[] {"Blue", "Red"});
    AutonomousTextOption strategy = new AutonomousTextOption("Strategy", "Do-It-All-Ramp", new String [] {"Do-It-All-Ramp", "Beacon-Ramp","Shooter-Angled","Shooter-Ramp","Blitz-Ramp"});
    AutonomousIntOption waitStart = new AutonomousIntOption("Wait at Start", 0, 0, 20);
    AutonomousBooleanOption park = new AutonomousBooleanOption("Park", true);

    AutonomousOption [] autoOptions = {};
    int currentOption = 0;

    public void initializeRobot(){
        /* Insert initialization code here */
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

        r.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        r.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        r.closeGrabber();
        r.lift.setPower(.7);
        Thread.sleep(250);
        r.lift.setPower(0);
        runEncoder(82 + 19.2/2,82 + 19.2/2,0,.325); // Go forward 3 1/4 ft.
        r.openGrabber();
        Thread.sleep(1000);
        runEncoder(19.2/3,19.2/3,0,-.325);
        //runEncoder(19.2,19.2,0,.325); // Go forward 1 ft.


        /*
        runEncoder(19.2/2,19.2/2,0,-.325); // 6 in. back
        r.jewel.setPosition(0);
        telemetry.addLine("Blue: " + r.colorSensor.blue());
        telemetry.addLine("Red: " + r.colorSensor.red());
        Thread.sleep(10000);
        */
        //r.openGrabber();

        //turn(48,.7); // About a 90 degree turn

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
            newLeftTarget = (Math.abs(r.leftMotor.getCurrentPosition()) + (int)(leftInches * r.ticksPerInch));
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

    private void turn(int distance,double speed){
        distance*=r.ticksPerInch;
        if (!opModeIsActive()) {
            stop();
        }
        if (isStopRequested()) {
            r.leftMotor.setPower(0);
            r.rightMotor.setPower(0);

        } else {
            boolean dir = r.leftMotor.getCurrentPosition() < distance;
            int ldirection = 1;
            int rdirection = -1;
            if(!dir){//go backwards
                ldirection = -1;
                rdirection = 1;
            }

            r.rightMotor.setPower(speed*rdirection);
            r.leftMotor.setPower(speed*ldirection);

            while (opModeIsActive() &&
                    (checkDistance(distance, r.leftMotor, dir) && checkDistance(-distance, r.rightMotor, !dir)));
            telemetry.addLine();
            // Stop all motion;
            r.rightMotor.setPower(0);
            r.leftMotor.setPower(0);
        }
    }
    private void runEncoder(double Lback, double Rback, double timeoutS, //change to calc timeout
                            double speed) {
        Lback*=r.ticksPerInch;
        Rback*=r.ticksPerInch;
        if (!opModeIsActive()) {
            stop();
        }
        if (isStopRequested()) {
            r.leftMotor.setPower(0);
            r.rightMotor.setPower(0);

        } else {
            boolean dir = r.leftMotor.getCurrentPosition() < Lback;
            int direction = 1;
            if(!dir){//go backwards
                direction = -1;
            }

            r.rightMotor.setPower(speed*direction);
            r.leftMotor.setPower(speed*direction);

            while (opModeIsActive() &&
                    (checkDistance(Lback, r.leftMotor, dir) && checkDistance(Rback, r.rightMotor, dir)));
            telemetry.addLine("STOP");
            telemetry.update();
            // Stop all motion;
            r.rightMotor.setPower(0);
            r.leftMotor.setPower(0);
        }
    }
    private boolean checkDistance(double tickTarget, DcMotor motor, boolean forwards){
        telemetry.addLine("" + (Math.abs(tickTarget) < Math.abs(motor.getCurrentPosition())));
        if(forwards)
            return Math.abs(tickTarget) > Math.abs(motor.getCurrentPosition());
        else
            return tickTarget < motor.getCurrentPosition();
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
