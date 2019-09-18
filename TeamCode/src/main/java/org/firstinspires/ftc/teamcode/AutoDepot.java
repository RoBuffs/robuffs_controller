package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Depot Start")
public class AutoDepot extends AutoModeParent4798 {

    public void runOpMode() throws InterruptedException {

        setup();

        //////////////////////////////////////////////////////
        waitForStart();
        //////////////////////////////////////////////////////

        detach();

        strafe(-ROTATION);

        move(FOOT);

        goldSpotted = scan(-1, 30); //LEFT SCAN

        if (goldSpotted) {

            move(Math.round(FOOT * 2.2f));
            turn( Math.round(ROTATION*1.4f));
            move(FOOT);
            dropMarker();
            move(Math.round(-FOOT * 0.25f));


        } else {

            move(Math.round(-FOOT * 0.25f));
            strafe(Math.round(FOOT * 0.5f));
            goldSpotted = scan(1, 30); //MIDDLE SCAN

            if(goldSpotted){

                move(FOOT * 3);
                dropMarker();
                move(-FOOT);

            } else {

                move(Math.round(-FOOT * 0.25f));
                strafe(Math.round(FOOT * 1.5f));
                //goldSpotted = scan(1); //RIGHT SCAN

                move(Math.round(FOOT * 2.2f));
                turn( Math.round(-ROTATION*1.4f));
                move(FOOT);
                dropMarker();
                move(Math.round(-FOOT * 0.25f));

            }

        }

        lowerArm();

    }

}
