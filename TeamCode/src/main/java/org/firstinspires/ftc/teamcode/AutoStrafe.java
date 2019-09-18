package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Autonomous(name="Strafe Test")
public class AutoStrafe extends LinearOpMode {

    //CONSTANTS
    int ROTATION = 1120;

    //HARDWARE
    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftRear;
    DcMotor rightRear;
    DcMotor arm;
    ColorSensor color;

    @Override
    public void runOpMode() throws InterruptedException {

        setup();

        ////////////////////////////////////////////
        waitForStart();
        ////////////////////////////////////////////

        /*
        int test = color.argb(); // argb = hue
        while(opModeIsActive()){

            telemetry.addLine();

        }
        */

        //DETACH
        arm.setTargetPosition(-7000);
        arm.setPower(0.6);
        while(arm.isBusy()){

            idle();

        }

        strafe(ROTATION);

        arm.setTargetPosition(0);
        arm.setPower(.8);
        while(arm.isBusy()){

            idle();

        }

        move(1120);

        //Move to the minerals
        //Scan middle mineral
        //If gold, knock it and either enter crater or depot
        //If silver, go to the inner mineral and scan
        //If silver again, go to outer mineral and knock it
        //Make sure to park somewhere, place marker if on marker side

    }

    public void strafe(int distance) throws InterruptedException {

        //POSITIVE = GO RIGHT
        leftFront.setTargetPosition(-distance);
        leftRear.setTargetPosition(distance);
        rightFront.setTargetPosition(-distance);
        leftFront.setTargetPosition(distance);

        rightFront.setPower(1);
        rightRear.setPower (1);
        leftFront.setPower (1);
        leftRear.setPower  (1);

        while(rightFront.isBusy() && rightRear.isBusy() && leftFront.isBusy() && leftRear.isBusy()){

            idle();

        }

        resetEncoders();



    }

    public void move( int distance ) throws InterruptedException {

        int error = distance;
        double P = .0001;
        int bias = 0;

        rightFront.setTargetPosition(distance);
        rightRear.setTargetPosition(distance);
        leftFront.setTargetPosition(distance);
        leftRear.setTargetPosition(distance);

        while(rightFront.isBusy() && rightRear.isBusy() && leftFront.isBusy() && leftRear.isBusy()){

            rightFront.setPower((error * P) + bias);
            rightRear.setPower ((error * P) + bias);
            leftFront.setPower ((error * P) + bias);
            leftRear.setPower  ((error * P) + bias);

            error = distance - rightFront.getCurrentPosition();
            telemetry.addData("Front Right Encoder", rightFront.getCurrentPosition());
            telemetry.addData("Back Right Encoder", rightRear.getCurrentPosition());
            telemetry.update();

        }

        // Set things back to zero
        resetEncoders();

    }

    public void resetEncoders() throws InterruptedException{

        // Stop the motors
        leftFront.setPower(0);
        leftRear.setPower(0);
        rightFront.setPower(0);
        rightRear.setPower(0);

        // Reset the encoders
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Reset to run mode
        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftRear.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightRear.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    }

    public void turn( int distance ) throws InterruptedException {



        // Set things back to zero
        resetEncoders();

    }

    //////DEPRECATED/////////
    public void turnRight( int distance ) throws InterruptedException {

        // Set things back to zero
        resetEncoders();

    }

    public void setup(){

        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftRear = hardwareMap.dcMotor.get("leftRear");
        rightRear = hardwareMap.dcMotor.get("rightRear");
        arm = hardwareMap.dcMotor.get("arm");

        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftRear.setDirection(DcMotorSimple.Direction.REVERSE);

        //color = hardwareMap.colorSensor.get("color");

        // We show the log in oldest-to-newest order, as that's better for poetry
        telemetry.log().setDisplayOrder(Telemetry.Log.DisplayOrder.OLDEST_FIRST);
        // We can control the number of lines shown in the log
        telemetry.log().setCapacity(6);

        // Reset the encoders
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Reset to run mode
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftRear.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightRear.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    }



}
