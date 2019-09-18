package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.SensorColor;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Disabled
@Autonomous(name="Automatic Meccy Boi")
public class AutoModeParent4798 extends LinearOpMode {

    public void halt(int milliseconds){

        ElapsedTime timer = new ElapsedTime();
        timer.reset();
        while(timer.milliseconds() < time && opModeIsActive()){
            idle();
        }

    }

    public void setup(){

        // We show the log in oldest-to-newest order, as that's better for poetry
        telemetry.log().setDisplayOrder(Telemetry.Log.DisplayOrder.OLDEST_FIRST);
        // We can control the number of lines shown in the log
        telemetry.log().setCapacity(6);

        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftRear = hardwareMap.dcMotor.get("leftRear");
        rightRear = hardwareMap.dcMotor.get("rightRear");
        arm = hardwareMap.dcMotor.get("arm");
        marker = hardwareMap.servo.get("marker");

        dist = hardwareMap.get(DistanceSensor.class, "dist");
        color = hardwareMap.colorSensor.get("color");

        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftRear.setDirection(DcMotorSimple.Direction.REVERSE);


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


    //CONSTANTS
    int ROTATION = 1120;
    int FOOT = 1680;

    //Other Stuff
    boolean goldSpotted;

    //HARDWARE
    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftRear;
    DcMotor rightRear;
    DcMotor arm;
    Servo marker;
    ColorSensor color;
    DistanceSensor dist;

    @Override
    public void runOpMode() throws InterruptedException {

        setup();

        ////////////////////////////////////////////
        waitForStart();
        ////////////////////////////////////////////

        //Move to the minerals
        //Scan middle mineral
        //If gold, knock it and either enter crater or depot
        //If silver, go to the inner mineral and scan
        //If silver again, go to outer mineral and knock it
        //Make sure to park somewhere, place marker if on marker side

    }

    public boolean scan (int direction, int mineralThreshold) throws InterruptedException {

        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftRear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightRear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        rightFront.setPower(-0.4*direction);
        rightRear.setPower (0.4*direction);
        leftFront.setPower (0.4*direction);
        leftRear.setPower  (-0.4*direction);

        while(dist.getDistance(DistanceUnit.CM) > mineralThreshold && opModeIsActive()){

            telemetry.addData("Dist", dist.getDistance(DistanceUnit.CM));
            telemetry.update();
            idle();

        }

        resetEncoders();
        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftRear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightRear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addData("Dist", dist.getDistance(DistanceUnit.CM));
        telemetry.update();

        halt(500);

        if(dist.getDistance(DistanceUnit.CM) > 55){

            move(250 * -direction);

        }

        double error = dist.getDistance(DistanceUnit.CM);
        double P = 0.000005;
        double bias = 0.1;

        while(error > 3 && opModeIsActive()){

            telemetry.addData("Dist", dist.getDistance(DistanceUnit.CM));
            telemetry.update();

            rightFront.setPower((error * P) + bias);
            rightRear.setPower ((error * P) + bias);
            leftFront.setPower ((error * P) + bias);
            leftRear.setPower  ((error * P) + bias);

            error = dist.getDistance(DistanceUnit.CM);

            idle();

        }


        resetEncoders();
        halt(500);

        if(color.red() >= color.blue()*2 && color.green() > color.blue()){

            return true;

        } else {

            return false;

        }

        // Inch forward until within 3cm.
        // Check color
        // If red > blue*2 then gold
        // Else silver, try again

    }

    public void strafe(int distance) throws InterruptedException {

        //POSITIVE = GO RIGHT
        leftFront.setTargetPosition(distance);
        leftRear.setTargetPosition(-distance);
        rightFront.setTargetPosition(-distance);
        rightRear.setTargetPosition(distance);

        rightFront.setPower(1);
        rightRear.setPower (1);
        leftFront.setPower (1);
        leftRear.setPower  (-1);

        while(rightFront.isBusy() && rightRear.isBusy() && leftFront.isBusy() && leftRear.isBusy() && opModeIsActive()){

            idle();

        }

        resetEncoders();

    }

    public void turn( int distance ) throws InterruptedException {

        int error = distance;
        double P = .0005;
        double bias = 0.33;

        rightFront.setTargetPosition(-distance);
        rightRear.setTargetPosition(-distance);
        leftFront.setTargetPosition(distance);
        leftRear.setTargetPosition(distance);

        while(rightFront.isBusy() && rightRear.isBusy() && leftFront.isBusy() && leftRear.isBusy() && opModeIsActive()){

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

    public void move( int distance ) throws InterruptedException {

        int error = distance;
        double P = .0005;
        double bias = 0.33;

        rightFront.setTargetPosition(distance);
        rightRear.setTargetPosition(distance);
        leftFront.setTargetPosition(distance);
        leftRear.setTargetPosition(distance);

        while(rightFront.isBusy() && rightRear.isBusy() && leftFront.isBusy() && leftRear.isBusy() && opModeIsActive()){

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

    public void detach(){

        arm.setTargetPosition(-7000);
        arm.setPower(1);
        while(arm.isBusy() && opModeIsActive()){

            idle();

        }

    }

    public void lowerArm(){

        arm.setTargetPosition(0);
        arm.setPower(1);
        while(arm.isBusy() && opModeIsActive()){

            idle();

        }

    }

    public void dropMarker(){

        marker.setPosition(0);

    }
}
