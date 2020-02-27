package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

import static java.lang.Thread.sleep;

public class AutoLib {
    Tommy robot = new Tommy();

    double speed = 0.75;

    // Set power forwards
    private void forwards() {
        robot.flDrive.setPower(speed);
        robot.frDrive.setPower(speed);
        robot.rlDrive.setPower(speed);
        robot.rrDrive.setPower(speed);
    }

    //Set power backwards
    private void backwards() {
        robot.flDrive.setPower(-speed);
        robot.frDrive.setPower(-speed);
        robot.rlDrive.setPower(-speed);
        robot.rrDrive.setPower(-speed);
    }

    //Set power strafe left
    private void leftStrafe() {
        robot.flDrive.setPower(speed);
        robot.frDrive.setPower(-speed);
        robot.rlDrive.setPower(-speed);
        robot.rrDrive.setPower(speed);
    }

    //Set power strafe right
    private void rightStrafe() {
        robot.flDrive.setPower(-speed);
        robot.frDrive.setPower(speed);
        robot.rlDrive.setPower(speed);
        robot.rrDrive.setPower(-speed);
    }

    //Set power clockwise turn
    private void clockwise() {
        robot.flDrive.setPower(speed);
        robot.frDrive.setPower(-speed);
        robot.rlDrive.setPower(speed);
        robot.rrDrive.setPower(-speed);
    }

    //Set power counter-clockwise turn
    private void cclockwise() {
        robot.flDrive.setPower(-speed);
        robot.frDrive.setPower(speed);
        robot.rlDrive.setPower(-speed);
        robot.rrDrive.setPower(speed);
    }

    //Stop all wheels
    private void stopWheels() {
        robot.flDrive.setPower(0);
        robot.frDrive.setPower(0);
        robot.rlDrive.setPower(0);
        robot.rrDrive.setPower(0);
    }

    //Go to encoder forwards/backwards
    private void move(int encoder, AutoOpMode callback) {
        resetEnc();

        if (robot.flDrive.getCurrentPosition() < encoder) {
            robot.flDrive.setTargetPosition(encoder);
            forwards();

            while (robot.flDrive.getCurrentPosition() <= robot.flDrive.getTargetPosition()) {
                if (!callback.opModeIsActive()) {
                    stopWheels();
                    return;
                }
            }
            stopWheels();
        } else {
            robot.flDrive.setTargetPosition(encoder);
            backwards();

            while (robot.flDrive.getCurrentPosition() >= robot.flDrive.getTargetPosition()) {
                if (!callback.opModeIsActive()) {
                    stopWheels();
                    return;
                }
            }
            stopWheels();

        }
    }

    //Go to encoder strafe
    private void strafe(int encoder, AutoOpMode callback) {
        resetEnc();

        if (robot.flDrive.getCurrentPosition() < encoder) {
            robot.flDrive.setTargetPosition(encoder);
            leftStrafe();

            while (robot.flDrive.getCurrentPosition() <= robot.flDrive.getTargetPosition()) {
                if (!callback.opModeIsActive()) {
                    stopWheels();
                    return;
                }
            }
            stopWheels();

        }

        if (robot.flDrive.getCurrentPosition() > encoder) {
            robot.flDrive.setTargetPosition(encoder);
            rightStrafe();

            while (robot.flDrive.getCurrentPosition() >= robot.flDrive.getTargetPosition()) {
                if (!callback.opModeIsActive()) {
                    stopWheels();
                    return;
                }
            }
            stopWheels();

        }
    }

    //Go to encoder rotate
    private void rotate(int degrees, AutoOpMode callback) {
        resetEnc();
        int encoder = degrees * 17;

        if (robot.flDrive.getCurrentPosition() > encoder) {
            robot.flDrive.setTargetPosition(encoder);
            cclockwise();

            while (robot.flDrive.getCurrentPosition() >= robot.flDrive.getTargetPosition()) {
                if (!callback.opModeIsActive()) {
                    stopWheels();
                    return;
                }
            }
            stopWheels();
        } else {
            robot.flDrive.setTargetPosition(encoder);
            clockwise();

            while (robot.flDrive.getCurrentPosition() <= robot.flDrive.getTargetPosition()) {
                if (!callback.opModeIsActive()) {
                    stopWheels();
                    return;
                }
            }
            stopWheels();
        }
    }

    //Reset encoders
    private void resetEnc() {
        robot.flDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.flDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.frDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rlDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rlDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rrDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rrDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    // FINGOR
    private void blockLift() throws InterruptedException {
        double i = 16;
        double pos;
        robot.finger.setPosition(0.7);
        sleep(500);
        while (i > 8) {
            pos = i / 20;
            robot.blockmover.setPosition(pos);
            i -= 1;
            sleep(75);
        }
    }
}

interface AutoOpMode {
    boolean opModeIsActive();
}