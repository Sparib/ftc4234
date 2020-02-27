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
@Autonomous(name="Block Drop Blue Wall", group="Blue")

public class DropBlue extends LinearOpMode {
    
    //Add drive variables DO NOT SET HERE
    private DcMotor flDrive;
    private DcMotor frDrive;
    private DcMotor rlDrive;
    private DcMotor rrDrive;
    
    private Servo platgrabber;
    private Servo finger;
    
    private double speed = 0.75;

    //Make Varis
    int tarPos;
    int curPos;

    @Override
    public void runOpMode() {
        
        //Set drive variables
        flDrive = hardwareMap.get(DcMotor.class, "flWheel");
        frDrive = hardwareMap.get(DcMotor.class, "frWheel");
        rlDrive = hardwareMap.get(DcMotor.class, "rlWheel");
        rrDrive = hardwareMap.get(DcMotor.class, "rrWheel");
        
        //PlatGrabber
        platgrabber = hardwareMap.get(Servo.class, "platmover");
        finger = hardwareMap.get(Servo.class, "finger");
        
        //Set directions
        flDrive.setDirection(DcMotor.Direction.REVERSE);
        rlDrive.setDirection(DcMotor.Direction.REVERSE);
        
        //Zero power
        flDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rlDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rrDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        
        //Reset encoders
        resetEnc();
        
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        // 0.18 is down, 0.7 is up (Finger)
        // 0.8 is down, 0.4 is up (Platgrabber)
        
        if (opModeIsActive()) {
            finger.setPosition(0);
            platgrabber.setPosition(0);
            //First Block
            //move(-320);
            //sleep(250);
            strafe(2900);
            platgrabber.setPosition(1);
            sleep(1000);
            strafe(-1280);
            blockLift();
            //Plaform
            move(3700);
            sleep(1000);
            finger.setPosition(0);
            move(-4200);
            
            
            
            
            
            
            
            
            //Second Block
            /*move(6250);
            strafe(1100);
            platgrabber.setPosition(0.8);
            sleep(1000);
            strafe(-1350);
            move(-7400);
            blockLift();
            sleep(3000);*/
        }
    }
    // Set power forwards
    private void forwards() {
        flDrive.setPower(speed);
        frDrive.setPower(speed);
        rlDrive.setPower(speed);
        rrDrive.setPower(speed);
    }
    
    //Set power backwards
    private void backwards() {
        flDrive.setPower(-speed);
        frDrive.setPower(-speed);
        rlDrive.setPower(-speed);
        rrDrive.setPower(-speed);
    }
    
    //Set power strafe left
    private void leftStrafe() {
        flDrive.setPower(speed);
        frDrive.setPower(-speed);
        rlDrive.setPower(-speed);
        rrDrive.setPower(speed);
    }
    
    //Set power strafe right
    private void rightStrafe() {
        flDrive.setPower(-speed);
        frDrive.setPower(speed);
        rlDrive.setPower(speed);
        rrDrive.setPower(-speed);
    }
    
    //Set power clockwise turn
    private void clockwise() {
        flDrive.setPower(speed);
        frDrive.setPower(-speed);
        rlDrive.setPower(speed);
        rrDrive.setPower(-speed);
    }
    
    //Set power counter-clockwise turn
    private void cclockwise() {
        flDrive.setPower(-speed);
        frDrive.setPower(speed);
        rlDrive.setPower(-speed);
        rrDrive.setPower(speed);
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
            flDrive.setTargetPosition(encoder);
            forwards();
            
            while (flDrive.getCurrentPosition() <= flDrive.getTargetPosition()) {
                if (!opModeIsActive()) {
                    stopWheels(); 
                    return;
                }
            }
            stopWheels();
        } else if (bigger == false) {
            flDrive.setTargetPosition(encoder);
            backwards();
            
            while (flDrive.getCurrentPosition() >= flDrive.getTargetPosition()) {
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
            flDrive.setTargetPosition(encoder);
            leftStrafe();
            
            while (flDrive.getCurrentPosition() <= flDrive.getTargetPosition()) {
                if (!opModeIsActive()) {
                    stopWheels();
                    return;
                }
            }
            stopWheels();
            
        }
        
        if (!bigger) {
            flDrive.setTargetPosition(encoder);
            rightStrafe();
            
            while (flDrive.getCurrentPosition() >= flDrive.getTargetPosition()) {
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
        resetEnc();
        int encoder = degrees * 17;
        boolean bigger;
        
        if (flDrive.getCurrentPosition() > encoder) {
            bigger = false;
        } else {
            bigger = true;
        }
        
        if (!bigger) {
            flDrive.setTargetPosition(encoder);
            cclockwise();
            
            while (flDrive.getCurrentPosition() >= flDrive.getTargetPosition()) {
                if (!opModeIsActive()) {
                    stopWheels();
                    return;
                }
            }
            stopWheels();
        } else {
            flDrive.setTargetPosition(encoder);
            clockwise();
            
            while (flDrive.getCurrentPosition() <= flDrive.getTargetPosition()) {
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
    
    private void blockLift() {
        double i = 20;
        double pos;
        finger.setPosition(1);
        while (i > -1) {
            pos = i / 20;
            telemetry.addData("Pos", pos);
            platgrabber.setPosition(pos);
            i -= 1;
            telemetry.addData("Grabber", platgrabber.getPosition());
            telemetry.update();
            sleep(75);
        }
    }
    
    //Set curPos
    /*
    private void getCur() {
        curPos = (Math.abs(flDrive.getCurrentPosition()) + Math.abs(frDrive.getCurrentPosition()) + Math.abs(rlDrive.getCurrentPosition()) + Math.abs(rrDrive.getCurrentPosition())) / 4;
    }*/
}
