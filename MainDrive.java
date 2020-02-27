package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import static java.lang.Math.PI;

@TeleOp(name="Main Drival", group="main")

public class MainDrive extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    private Tommy robot = new Tommy();
    private Driive driving = new Driive();

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        BNO055IMU.Parameters params = new BNO055IMU.Parameters();
        params.angleUnit            = BNO055IMU.AngleUnit.RADIANS;
        params.accelUnit            = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        params.calibrationDataFile  = "BNO055IMUCalibration.json";
        params.loggingEnabled       = true;
        params.loggingTag           = "IMU";
        params.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        robot.imu.initialize(params);

        DcMotor[] motors = {robot.frDrive, robot.rrDrive, robot.rlDrive, robot.flDrive};
        double[] angles = {PI/4, 3*PI/4, 5*PI/4, 7*PI/4};

        try {
            driving.init(motors, angles);
        } catch(Exception e) {}    
        
        driving.speed = 10;
        //Servo stuffs
        robot.blockmover.setPosition(-1);
        boolean blockup = false;
        
        //Switch
        boolean grabberr = false;
        boolean platwait = false;
        boolean rotater = false;
        boolean rotatec = false;
        boolean manual = false;
        boolean fingerc = false;
        boolean pmc = false;
        boolean pma = false;
        
        // Wait for the game to start (driver presses PLAY)
        //armReset();
        
        waitForStart();
        runtime.reset();

        Gamepad prev1 = new Gamepad();
        Gamepad prev2 = new Gamepad();
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            driving.cartesian(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
            driving.gyro(-robot.gangles.thirdAngle);
            driving.driive();
            
           double arm = gamepad2.left_stick_y;
           arm = Range.clip(arm, -1.0, 1.0);

           // Intake Control
            if (gamepad1.right_bumper) {
                robot.intake1.setPower(1);
                robot.intake2.setPower(1);
            } else if (gamepad1.left_bumper) {
                robot.intake1.setPower(-1);
                robot.intake2.setPower(-1);
            } else {
                robot.intake1.setPower(0);
                robot.intake2.setPower(0);
            }
            
            // Platform Arm Switch G1.A
            if (gamepad1.a && !prev1.a && !blockup) {
                robot.blockmover.setPosition(1);
                blockup = true;
            } else if (gamepad1.a && !prev1.a && blockup) {
                robot.blockmover.setPosition(-1);
                blockup = false;
            }
            
            // Finger Switch G1.Y
            if (gamepad1.y && !fingerc && !prev1.y) {
                robot.finger.setPosition(1);
                robot.blockmover.setPosition(0);
                fingerc = true;
            } else if (gamepad1.y && fingerc && !prev1.y){
                robot.finger.setPosition(0);
                fingerc = false;
            }

            int speed = driving.speed;
            
            // Faster G1.B
            if (gamepad1.b && !prev1.b && driving.speed < 10) driving.speed = speed + 2;
            
            // Slow Switch G1.X
            if (gamepad1.x && !prev1.x && driving.speed > 2) driving.speed = speed - 2;
            
            // Grabber Switch G2.B
            if (gamepad2.b && !prev2.b && !grabberr) {
                robot.grabber.setPosition(1);
                grabberr = true;
            } else if (gamepad2.b && !prev2.b && grabberr) {
                robot.grabber.setPosition(-1);
                grabberr = false;
            }
            
            // Rotate Switch G2.Y
            if (gamepad2.y && !rotater && !prev2.y && !manual) {
                rotatorOut();
                rotater = true;
                rotatec = true;
            } else if (gamepad2.y && rotater && !prev2.y && !manual) {
                rotatorIn();
                rotater = false;
                rotatec = true;
            }
            
            if (!robot.rotator.isBusy()) {
                robot.rotator.setPower(0);
                rotatec = false;
            }
            
            if (robot.rotator.isBusy() && !rotatec) {
                if (!rotater) {
                    robot.rotator.setPower(0.05);
                } else if (rotater) {
                    robot.rotator.setPower(-0.05);
                }
            }

            robot.slider.setPower(arm);
            
            //Manual Move G2.X
            if (gamepad2.x && !manual && !prev2.x) {
                manual = true;
                robot.rotator.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            } else if (gamepad2.x && manual && !prev2.x) {
                manual = false;
                robot.rotator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                robot.rotator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }
            
            if (manual) {
                robot.rotator.setPower(gamepad2.right_stick_x / 2);
            }
            
            //Platformmover Button Switch G1.RT
            if (gamepad1.right_trigger > 0)     pma = true;
            if (gamepad1.right_trigger == 0)    pma = false;    platwait = false;
            
            // Platformmover Switch
            if (pma && !pmc && !platwait) {
                pmc = true;
                platwait = true;
                robot.platformmover.setPosition(0.5);
            } else if (pma && pmc && !platwait) {
                pmc = false;
                platwait = true;
                robot.platformmover.setPosition(-1);
            }

            //Reset Switch G1.Start
            if (gamepad1.start) driving.resetZero();

            if (gamepad1.back) robot.resetEnc();

            telemetry.addData("cur", robot.rotator.getCurrentPosition());
            telemetry.addData("tar", robot.rotator.getTargetPosition());
            telemetry.update();

            try {
                prev1.copy(gamepad1);
                prev2.copy(gamepad2);
            } catch (RobotCoreException e) {
                idle();
            }

            robot.updateGyro();
        }
    }
    
    private void rotatorOut() {
        robot.rotator.setTargetPosition(-395);
        robot.rotator.setPower(0.1);
    }
    
    private void rotatorIn() {
        robot.rotator.setTargetPosition(2);
        robot.rotator.setPower(-0.1);
    }
}
/*
 * Wheel testing notes:
 *
 * - Right being negative is clockwise spinning
 * - FL/RR when negative strafes right
 *
 */
