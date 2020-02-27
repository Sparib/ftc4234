package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp(name="Graberer", group="no")

public class Grabberer extends LinearOpMode {
    Tommy robot = new Tommy();

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);

        Gamepad prev1 = new Gamepad();
        prev1 = gamepad1;

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.b && !prev1.b) robot.grabber.setPosition(1);
            if (gamepad1.x && !prev1.x) robot.grabber.setPosition(0);
        }
    }
}
