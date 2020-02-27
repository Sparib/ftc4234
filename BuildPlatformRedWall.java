/*
Copyright 2019 FIRST Tech Challenge Team 4234

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import static java.lang.Math.PI;

/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a PushBot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Remove a @Disabled the on the next line or two (if present) to add this opmode to the Driver Station OpMode list,
 * or add a @Disabled annotation to prevent this OpMode from being added to the Driver Station
 */
@Autonomous(name="PlatRedWall", group="Red")

public class BuildPlatformRedWall extends LinearOpMode implements TeleAuto {
    //Make Varis
    double speed = 0.75;

    // Add Libs
    Tommy robot = new Tommy();
    Driive driving = new Driive();

    @Override
    public void runOpMode() {
        // Initalize Robot
        robot.init(hardwareMap);

        //Add field things
        double[] angles = {PI/4, 3*PI/4, 5*PI/4, 7*PI/4};
        DcMotor[] motors = {robot.frDrive, robot.rrDrive, robot.rlDrive, robot.flDrive};

        //Reset encoders
        robot.resetEnc();

        driving.righteous = true;

        try {
            driving.init(motors, angles);
        } catch (Exception e) {}


        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        /* 0.18 is down, 0.7 is up (Finger)
         * 0.8 is down, 0.4 is up (Platgrabber)
         */

        double pi = Math.PI;

        // -----START OF PROGRAM-----
        driving.polarAuto(0.75, -pi/2, 700, this);
        driving.polarAuto(0.75, 0, 1700, this);
        robot.platformmover.setPosition(-1);
        sleep(1000);
        driving.polarAuto(0.75, pi, 1800, this);
        robot.platformmover.setPosition(0.5);
        driving.polarAuto(0.75, pi/2, 3500, this);

    }
    // FINGOR
    private void blockLift() {
        double i = 16;
        double pos;
        robot.finger.setPosition(0.7);
        sleep(500);
        while (i > 8) {
            pos = i / 20;
            telemetry.addData("Pos", pos);
            robot.blockmover.setPosition(pos);
            i -= 1;
            telemetry.addData("Grabber", robot.blockmover.getPosition());
            telemetry.update();
            sleep(75);
        }
    }

    private void updateAuto() {
        robot.updateGyro();
        driving.gyro(robot.gangles.secondAngle);
    }
}
