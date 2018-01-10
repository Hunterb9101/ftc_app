package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Hunter on 11/3/2016.
 */

@TeleOp(name="Qualifier TeleOp",group="LinearOpMode")
public class TeleOpQualifier12239 extends LinearOpMode {
    Hardware r = new Hardware();
    public void runOpMode() throws InterruptedException {
        r.init(hardwareMap); // Initializes robot hardware
        waitForStart();
        while (opModeIsActive()) {
            // DRIVE FUNCTION //
            double leftPower = powerAdjust(-gamepad1.left_stick_y, .75, .1);
            double rightPower = powerAdjust(-gamepad1.right_stick_y, .75, .1);
            telemetry.addLine("Left Power: " + leftPower);
            telemetry.addLine("RightPower: " + rightPower);
            telemetry.update();
            r.leftMotor.setPower(leftPower);
            r.rightMotor.setPower(rightPower);


            // Lift Mechanism
            if(gamepad2.y){
                r.lift.setPower(.7);
            }
            else if(gamepad2.a){
                r.lift.setPower(-.3);
            }
            else{
                r.lift.setPower(0);
            }

            // Old grabber
            // Closed: .4 .6
            // Open: .8 .2
            if(gamepad2.x){ //Close grabber
                r.closeGrabber();
            }
            else if(gamepad2.b){ //Open Grabber
                r.openGrabber();
            }
        }
    }

    // Power Adjust allows for a dead-zone in the center of the joystick //
    public double powerAdjust(double vMotorPower, double iPowerMax, double iThreshHold){
        if (Math.abs(vMotorPower) < iThreshHold) {
            vMotorPower = 0;
        }

        else
        {
            if (vMotorPower< 0)
            {
                vMotorPower= vMotorPower+ iThreshHold;
            }
            else
            {
                vMotorPower= vMotorPower-iThreshHold;
            }
            vMotorPower= (vMotorPower*iPowerMax) / (1.0-iThreshHold);
        }
        return vMotorPower;
    }
}