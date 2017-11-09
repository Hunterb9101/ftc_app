package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by Hunter on 11/3/2016.
 */

@TeleOp(name="Qualifier TeleOp",group="LinearOpMode")
public class TeleOpExample extends LinearOpMode {
    Hardware r = new Hardware();
    public void runOpMode() throws InterruptedException {
        r.init(hardwareMap); // Initializes robot hardware
        waitForStart();
        boolean yPressed = false;
        while (opModeIsActive()) {
            // DRIVE FUNCTION //
            double leftPower = powerAdjust(-gamepad1.left_stick_y, 1.0, .2);
            double rightPower = powerAdjust(-gamepad1.right_stick_y, 1.0, .2);
            r.leftMotor.setPower(leftPower);
            r.rightMotor.setPower(rightPower);

            if(gamepad1.a){
                r.lift.setPower(.5);
            }
            else if(gamepad1.y){
                r.lift.setPower(-.5);
            }
            else{
                r.lift.setPower(0);
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
            vMotorPower= Math.floor((vMotorPower*iPowerMax) / (1.0-iThreshHold));
        }
        return vMotorPower;
    }
}