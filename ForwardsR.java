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

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

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
@Autonomous(name="ForwardsR", group="None")

public class ForwardsR extends LinearOpMode {
    
    //Add drive variables DO NOT SET HERE
    private DcMotor flDrive;
    private DcMotor frDrive;
    private DcMotor rlDrive;
    private DcMotor rrDrive;

    @Override
    public void runOpMode() {
        
        //Set drive variables
        flDrive = hardwareMap.get(DcMotor.class, "flWheel");
        frDrive = hardwareMap.get(DcMotor.class, "frWheel");
        rlDrive = hardwareMap.get(DcMotor.class, "rlWheel");
        rrDrive = hardwareMap.get(DcMotor.class, "rrWheel");
        
        //Set directions
        flDrive.setDirection(DcMotor.Direction.REVERSE);
        rlDrive.setDirection(DcMotor.Direction.REVERSE);
        
        
        //Reset encoders
        resetEnc();
        
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        if (opModeIsActive()) {
            move(1500);
            sleep(250);
            strafe(3000);
        }
    }
    // Set power forwards
    private void forwards() {
        flDrive.setPower(0.5);
        frDrive.setPower(0.5);
        rlDrive.setPower(0.5);
        rrDrive.setPower(0.5);
    }
    
    //Set power backwards
    private void backwards() {
        flDrive.setPower(-0.5);
        frDrive.setPower(-0.5);
        rlDrive.setPower(-0.5);
        rrDrive.setPower(-0.5);
    }
    
    //Set power strafe left
    private void leftStrafe() {
        flDrive.setPower(0.5);
        frDrive.setPower(-0.5);
        rlDrive.setPower(-0.5);
        rrDrive.setPower(0.5);
    }
    
    //Set power strafe right
    private void rightStrafe() {
        flDrive.setPower(-0.5);
        frDrive.setPower(0.5);
        rlDrive.setPower(0.5);
        rrDrive.setPower(-0.5);
    }
    
    //Set power clockwise turn
    private void clockwise() {
        flDrive.setPower(0.5);
        frDrive.setPower(-0.5);
        rlDrive.setPower(0.5);
        rrDrive.setPower(-0.5);
    }
    
    //Set power counter-clockwise turn
    private void cclockwise() {
        flDrive.setPower(-0.5);
        frDrive.setPower(0.5);
        rlDrive.setPower(-0.5);
        rrDrive.setPower(0.5);
    }
    
    //Stop all wheels
    private void stopWheels() {
        flDrive.setPower(0);
        frDrive.setPower(0);
        rlDrive.setPower(0);
        rrDrive.setPower(0);
    }
    
    //Go to encoder forwards/backwards
    private void move(int encoder) {
        resetEnc();
        boolean bigger;
        
        if (flDrive.getCurrentPosition() > encoder) {
            bigger = false;
        } else {
            bigger = true;
        }
        
        if (bigger == true) {
            forwards();
            
            while (flDrive.getCurrentPosition() <= encoder) {
                if (!opModeIsActive()) {
                    stopWheels(); 
                    return;
                }
            }
            stopWheels();
        } else if (bigger == false) {
            backwards();
            
            while (flDrive.getCurrentPosition() >= encoder) {
                if (!opModeIsActive()) {
                    stopWheels();
                    return;
                }
            }
            stopWheels();
            
        }
    }
    
    //Go to encoder strafe
    private void strafe(int encoder) {
        resetEnc();
        boolean bigger;
        
        if (flDrive.getCurrentPosition() > encoder) {
            bigger = false;
        } else {
            bigger = true;
        }
        
        if (bigger) {
            leftStrafe();
            
            while (flDrive.getCurrentPosition() <= encoder) {
                if (!opModeIsActive()) {
                    stopWheels();
                    return;
                }
            }
            stopWheels();
            
        }
        
        if (!bigger) {
            rightStrafe();
            
            while (flDrive.getCurrentPosition() >= encoder) {
                if (!opModeIsActive()) {
                    stopWheels();
                    return;
                }
            }
            stopWheels();
            
        }
    }
    
    //Go to encoder rotate
    private void rotate(int degrees) {
        int encoder = degrees * 17 + flDrive.getCurrentPosition();
        boolean bigger;
        
        if (flDrive.getCurrentPosition() > encoder) {
            bigger = false;
        } else {
            bigger = true;
        }
        
        if (!bigger) {

            cclockwise();
            
            while (flDrive.getCurrentPosition() >= encoder) {
                if (!opModeIsActive()) {
                    stopWheels();
                    return;
                }
            }
            stopWheels();
        } else {
            clockwise();
            
            while (flDrive.getCurrentPosition() <= encoder) {
                if (!opModeIsActive()) {
                    stopWheels();
                    return;
                }
            }
            stopWheels();
        }
    }
    
    //Reset encoders
    private void resetEnc() {
        flDrive.setMode(DcMotor.RunMode.RESET_ENCODERS);
        flDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frDrive.setMode(DcMotor.RunMode.RESET_ENCODERS);
        frDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rlDrive.setMode(DcMotor.RunMode.RESET_ENCODERS);
        rlDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rrDrive.setMode(DcMotor.RunMode.RESET_ENCODERS);
        rrDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    
}
