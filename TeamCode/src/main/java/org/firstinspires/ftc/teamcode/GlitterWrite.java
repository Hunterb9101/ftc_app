package org.firstinspires.ftc.teamcode;

        import android.content.Context;
        import android.media.MediaScannerConnection;
        import android.net.Uri;
        import android.os.Environment;

        import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
        import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
        import com.qualcomm.robotcore.hardware.DcMotor;

        import java.io.File;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.io.OutputStreamWriter;
        import java.util.ArrayList;


@TeleOp(name = "GlitterCmds Write", group = "Linear Opmode")  // @Autonomous(...) is the other common choice
//@Disabled
public class GlitterWrite extends LinearOpMode {
    GlitterHardware r = new GlitterHardware();

    enum Dir{LEFTRIGHT,FORWARDBACK}
    Dir direction = null;  // Which way is the robot going to go?

    ArrayList<String> commands=new ArrayList<>(); // All commands that the robot will repeat

    @Override
    public void runOpMode() {
        boolean aPressed = false; // These seem like counterintuitive variables, but they make sure that
        boolean bPressed = false; // the code has updated fully before executing the code again
        boolean xPressed = false;

        r.init(hardwareMap);

        waitForStart();

        double lDist = 0; // Distance that the left motor travels in inches
        double rDist = 0; // Distance that the right motor travels in inches
        double increment = 1; // The distance added when the button is pressed

        telemetry.addData("Status", "Resetting Encoders");
        telemetry.update();

        r.leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        r.rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        idle();

        commands.add("// GLITTER RUN STARTED //");
        while (opModeIsActive()){
            // Direction Selection
            if((gamepad1.left_bumper || gamepad1.right_bumper) && (lDist == 0 && rDist == 0)){
                if(direction == Dir.FORWARDBACK){
                    direction = Dir.LEFTRIGHT;
                }
                else{
                    direction = Dir.FORWARDBACK;
                }
            }

            // Distance Selection
            if(gamepad1.dpad_up){
                if(direction == Dir.FORWARDBACK){
                    lDist+=increment;
                    rDist+=increment;
                    try {
                        Thread.sleep(250);
                    }
                    catch(InterruptedException e){}
                }
                else{
                    lDist+=increment;
                    rDist-=increment;
                    try {
                        Thread.sleep(250);
                    }
                    catch(InterruptedException e){}
                }
            }
            else if(gamepad1.dpad_down){
                if(direction == Dir.FORWARDBACK){
                    lDist-=increment;
                    rDist-=increment;
                    try {
                        Thread.sleep(250);
                    }
                    catch(InterruptedException e){}
                }
                else{
                    lDist-=increment;
                    rDist+=increment;
                    try {
                        Thread.sleep(250);
                    }
                    catch(InterruptedException e){}
                }
            }

            // Increment for Direction Selection
            if(gamepad1.dpad_right){
                increment*=2;
                try {
                    Thread.sleep(250);
                }
                catch(InterruptedException e){}
            }
            else if(gamepad1.dpad_left){
                increment/=2;
                try {
                    Thread.sleep(250);
                }
                catch(InterruptedException e){}
            }

            // Execute movement and clear data
            if(gamepad1.a && ! aPressed){
                encoderDrive(.325,lDist,rDist);
                appendCommand(lDist,rDist);
                direction = null;
                lDist = 0;
                rDist = 0;
                increment = 1;
                aPressed = true;
                telemetry.addLine("Finished Movement");
                telemetry.update();
                try {
                    Thread.sleep(500);
                }
                catch(InterruptedException e){}
            }else {
                aPressed = gamepad1.a;
            }

            // Comment Added
            if(gamepad1.b && ! bPressed){
                commands.add("//Insert Special Code Here");
                telemetry.addLine("Comment Added");
                telemetry.update();
                try {
                    Thread.sleep(500);
                }
                catch(InterruptedException e){}
            }else{
                bPressed=gamepad1.b;
            }

            // Clear Movement
            if(gamepad1.x && ! xPressed){
                direction = null;
                lDist = 0;
                rDist = 0;
                increment = 1;
            }else{
                xPressed =gamepad1.x;
            }

            // Stop Program
            if(gamepad1.y){
                break;
            }

            // Display data to the driver
            telemetry.addLine((direction == Dir.FORWARDBACK)?"Driving Straight":"Turning");
            telemetry.addLine("Left: " + lDist + " in." + ", Right: " + rDist + " in." );
            telemetry.addLine("Current Increment: " + increment);
            telemetry.update();
        }

        // Add commands to the file
        File DCIMPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File cmds = createFile(DCIMPath, "Commands.txt");
        update(hardwareMap.appContext,cmds);
        write(cmds, commands);
    }

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
            while (opModeIsActive() && (r.leftMotor.isBusy() && r.rightMotor.isBusy())) {}

            // Stop all motion;
            r.leftMotor.setPower(0);
            r.rightMotor.setPower(0);

            r.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            r.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            sleep(250);
        }
    }

    /////////////////////////////////////////
    //            File Methods             //
    //   What writes everything to a file  //
    /////////////////////////////////////////
    private void appendCommand(double lDist, double rDist){
        commands.add("encoderDrive(" + .325 + "," + lDist + "," + rDist + ")");
    }

    public static void write(File file, ArrayList<String> text){
        try {
            FileOutputStream fOut = new FileOutputStream(file,true);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.write(" ");
            for(int i = 0; i<text.size(); i++){
                myOutWriter.append(text.get(i) + ";\n");
            }
            myOutWriter.close();
            fOut.close();
        }
        catch (IOException e) {
            System.err.println("FILE ERROR");
        }
    }

    public static File createFile(File path, String filename){
        File file = new File(path, filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ignored) {
                ignored.printStackTrace();
            }
        }
        return file;
    }

    public static void update(Context context, File file){
        MediaScannerConnection.scanFile(
                context,
                new String[]{file.getAbsolutePath()},
                null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
    }
}