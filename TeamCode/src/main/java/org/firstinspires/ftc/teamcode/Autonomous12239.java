package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by Hunter on 7/30/2017.
 */
@Autonomous(name="Autonomous 12239",group="LinearOpMode")
public class Autonomous12239 extends LinearOpMode {
    VuforiaLocalizer vuforia;
    VuforiaTrackables relicTrackables;
    VuforiaTrackable relicTemplate;

    Hardware r = new Hardware();

    AutonomousTextOption allianceColor = new AutonomousTextOption("Alliance Color", "blue", new String[] {"Blue", "Red"});
    AutonomousTextOption strategy = new AutonomousTextOption("Strategy", "Do-It-All-Ramp", new String [] {"Do-It-All-Ramp", "Beacon-Ramp","Shooter-Angled","Shooter-Ramp","Blitz-Ramp"});
    AutonomousIntOption waitStart = new AutonomousIntOption("Wait at Start", 0, 0, 20);
    AutonomousBooleanOption park = new AutonomousBooleanOption("Park", true);

    AutonomousOption [] autoOptions = {};
    int currentOption = 0;

    public void initializeRobot(){
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters.vuforiaLicenseKey = "AWSQJ+r/////AAAAGYcszN6Ft09NjOfMIOYU7GVYST+3LAn19fK2ZZXyGCiCCEriemUEUxfjxylCI513aUjKneAYvaOFV45erEYoTOvWK+AXlbPqOibtK6n+FSuMBCdCiSpBOsVkjOzE4hZOiImdyxfvY3MEi3/BRRwpO6SrLMFVxn2kC0RUA/ArQDy0QLVDtqVMd/A8wUi7WboDQXmpB5Nq0BykMq5jrcmnwjn9VRzBzw0rIBAY2spRFP70LOpEdOf8IfOSZLexhGHnfX8ejwedgKcyoJRBmU7DfqZYqj/7JFR+ufkXeApKmM/12pYRWexi8GlsXmTn8DRxhGDH0KZ3w6dQWWFfSVMuXyw0av5bS7Bk3Dnsmgsd4X8/";

        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK; // Back camera has greater range
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary


    }

    public void runOpMode() throws InterruptedException{
        r.init(hardwareMap);
        initializeRobot();
        waitForStart();


        // RESET ENCODERS //
        telemetry.addData("Status", "Resetting Encoders");
        telemetry.update();

        r.leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        r.rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        idle();

        r.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        r.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        runEncoder(24,24,0,.325); // Go forward 1 ft.
        Thread.sleep(1000);
        runEncoder(12,12,0,-.325); // Go forward 1 ft.
        /*
        turn(90,.325);
        Thread.sleep(1000);
        turn(-90,.325);
        */
        /*
        // VUFORIA //
        relicTrackables.activate();

        // vuMark will return UNKNOWN,LEFT,CENTER, or RIGHT.
        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);

        if (vuMark == RelicRecoveryVuMark.UNKNOWN) {
            telemetry.addData("VuMark", "%s visible", vuMark);
        }
        else {
            telemetry.addData("VuMark", "not visible");
        }
        telemetry.update();

        // JEWEL Knockoff Code
        r.jewel.setPosition(0);
        telemetry.addLine("Blue: " + r.colorSensor.blue());
        telemetry.addLine("Red: " + r.colorSensor.red());

        // Grab block
        r.closeGrabber();
        r.lift.setPower(.7);
        Thread.sleep(250);
        r.lift.setPower(0);

        // Drive Forward (3ft for center, 2ft 4 in for Left, 3 ft 8 in for Right)
        // Turn 90 degrees
        // Forward 1 ft
        // open grabber
        // back up

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
        distance*=r.ticksPerInch * r.inchesPerDegrees;
        int[] startPos = new int[]{r.leftMotor.getCurrentPosition(),r.rightMotor.getCurrentPosition()};
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
                    (checkDistance(distance + startPos[0], r.leftMotor, dir) && checkDistance(-distance + startPos[1], r.rightMotor, !dir)));
            telemetry.addLine();
            // Stop all motion;
            r.rightMotor.setPower(0);
            r.leftMotor.setPower(0);
        }
    }
    private void runEncoder(double Lback, double Rback, double timeoutS, //change to calc timeout
                            double speed) {
        int[] startPos = new int[]{r.leftMotor.getCurrentPosition(),r.rightMotor.getCurrentPosition()};
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
                    (checkDistance(Lback + startPos[0], r.leftMotor, dir) && checkDistance(Rback + startPos[1], r.rightMotor, dir)));
            telemetry.addLine("STOP");
            telemetry.update();
            // Stop all motion;
            r.rightMotor.setPower(0);
            r.leftMotor.setPower(0);
        }
    }
    private boolean checkDistance(double tickTarget, DcMotor motor, boolean forwards){
        if(forwards)
            return tickTarget > motor.getCurrentPosition();
        else
            return tickTarget > motor.getCurrentPosition();
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
