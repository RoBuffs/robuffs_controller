package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp(name = "4798_OLD", group = "Competition")
public class ControlMode4798 extends OpMode {

    //Initialize Motor Variables
    DcMotor left;
    DcMotor right;

    DcMotor arm;

    @Override
    public void init(){
        //get references to the motors from hardware map
        left = hardwareMap.dcMotor.get("left");
        right = hardwareMap.dcMotor.get("right");

        arm = hardwareMap.dcMotor.get("arm");

        // We show the log in oldest-to-newest order, as that's better for poetry
        telemetry.log().setDisplayOrder(Telemetry.Log.DisplayOrder.OLDEST_FIRST);
        // We can control the number of lines shown in the log
        telemetry.log().setCapacity(6);

    }
    @Override
    public void loop(){

        //get controller values
        float leftY1 = gamepad1.left_stick_y;
        float rightY1 = gamepad1.right_stick_y;

        float leftY2 = gamepad2.left_stick_y;

        //set motors w/ value
        left.setPower(leftY1);
        right.setPower(-rightY1);
        arm.setPower(leftY2);

        /*
            If power is within a range
            so not within .1 to -.1
         */

    }

}
