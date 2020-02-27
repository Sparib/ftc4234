package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class Tommy {
    // Drive Motors:
    DcMotor flDrive         = null;
    DcMotor frDrive         = null;
    DcMotor rlDrive         = null;
    DcMotor rrDrive         = null;

    // Intake Motors:
    DcMotor intake1         = null;
    DcMotor intake2         = null;

    // Arm Parts:
    DcMotor slider          = null;
    DcMotor rotator         = null;
    Servo   grabber         = null;

    // Other Grabbers:
    Servo   platformmover   = null;
    Servo   blockmover      = null;
    Servo   finger          = null;

    // Field Centricity
    BNO055IMU imu;
    Orientation gangles;
    Acceleration gravity;

    void init(HardwareMap map) {

        // Drive Motors:
        flDrive = map.get(DcMotor.class, "flWheel");
        frDrive = map.get(DcMotor.class, "frWheel");
        rlDrive = map.get(DcMotor.class, "rlWheel");
        rrDrive = map.get(DcMotor.class, "rrWheel");

        // Intake:
        intake1 = map.get(DcMotor.class, "intake1");
        intake2 = map.get(DcMotor.class, "intake2");

        // Arm:
        slider = map.get(DcMotor.class, "slider");
        rotator = map.get(DcMotor.class, "rotator");
        grabber = map.get(Servo.class, "grabber");

        // Grabbers:
        platformmover = map.get(Servo.class, "platformmover");
        blockmover = map.get(Servo.class, "blockmover");
        finger = map.get(Servo.class, "finger");

        // Drive Modes:
        flDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rlDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rrDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // ZeroPowerBehavior:
        flDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rlDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rrDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slider.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        intake1.setDirection(DcMotor.Direction.REVERSE);
        rotator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rotator.setDirection(DcMotor.Direction.REVERSE);
        rotator.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        imu = map.get(BNO055IMU.class, "imu 1");

        updateGyro();
        resetEnc();
    }

    void updateGyro() {
        gangles = imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.RADIANS);
        gravity = imu.getGravity();
    }

    void resetEnc() {
        flDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rlDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rlDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rrDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rrDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}
