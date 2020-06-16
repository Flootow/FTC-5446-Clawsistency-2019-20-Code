package org.firstinspires.ftc.teamcode.Team5446.Competition;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.enumclaw.ftc.teamcode.Encoder;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MotionProfileAuto extends OpMode {
    Bot5446 bot = null;
    private ElapsedTime runtime = new ElapsedTime();

    enum ProgramMode{
        MAX_TEST,
        STRAIGHT;
    }
    ProgramMode programMode;
    List<Float> positions = new List<Float>() {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @Override
        public Iterator<Float> iterator() {
            return null;
        }

        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return null;
        }

        @Override
        public boolean add(Float aFloat) {
            return false;
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(Collection<? extends Float> c) {
            return false;
        }

        @Override
        public boolean addAll(int index, Collection<? extends Float> c) {
            return false;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public Float get(int index) {
            return null;
        }

        @Override
        public Float set(int index, Float element) {
            return null;
        }

        @Override
        public void add(int index, Float element) {

        }

        @Override
        public Float remove(int index) {
            return null;
        }

        @Override
        public int indexOf(Object o) {
            return 0;
        }

        @Override
        public int lastIndexOf(Object o) {
            return 0;
        }

        @Override
        public ListIterator<Float> listIterator() {
            return null;
        }

        @Override
        public ListIterator<Float> listIterator(int index) {
            return null;
        }

        @Override
        public List<Float> subList(int fromIndex, int toIndex) {
            return null;
        }
    };
    List<Float> velocities = new List<Float>() {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @Override
        public Iterator<Float> iterator() {
            return null;
        }

        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return null;
        }

        @Override
        public boolean add(Float aFloat) {
            return false;
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(Collection<? extends Float> c) {
            return false;
        }

        @Override
        public boolean addAll(int index, Collection<? extends Float> c) {
            return false;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public Float get(int index) {
            return null;
        }

        @Override
        public Float set(int index, Float element) {
            return null;
        }

        @Override
        public void add(int index, Float element) {

        }

        @Override
        public Float remove(int index) {
            return null;
        }

        @Override
        public int indexOf(Object o) {
            return 0;
        }

        @Override
        public int lastIndexOf(Object o) {
            return 0;
        }

        @Override
        public ListIterator<Float> listIterator() {
            return null;
        }

        @Override
        public ListIterator<Float> listIterator(int index) {
            return null;
        }

        @Override
        public List<Float> subList(int fromIndex, int toIndex) {
            return null;
        }
    };
    List<Float> accelerations = new List<Float>() {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @Override
        public Iterator<Float> iterator() {
            return null;
        }

        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return null;
        }

        @Override
        public boolean add(Float aFloat) {
            return false;
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(Collection<? extends Float> c) {
            return false;
        }

        @Override
        public boolean addAll(int index, Collection<? extends Float> c) {
            return false;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public Float get(int index) {
            return null;
        }

        @Override
        public Float set(int index, Float element) {
            return null;
        }

        @Override
        public void add(int index, Float element) {

        }

        @Override
        public Float remove(int index) {
            return null;
        }

        @Override
        public int indexOf(Object o) {
            return 0;
        }

        @Override
        public int lastIndexOf(Object o) {
            return 0;
        }

        @Override
        public ListIterator<Float> listIterator() {
            return null;
        }

        @Override
        public ListIterator<Float> listIterator(int index) {
            return null;
        }

        @Override
        public List<Float> subList(int fromIndex, int toIndex) {
            return null;
        }
    };
    boolean maxTestActive = true;

    float maxVelocityRecorded = 0;
    float maxAccelerationRecorded = 0;
    float maxDistanceRecorded = 0;
    float maxTestingTime = 0;

    @Override
    public void init()
    {
        telemetry.addData("Status", "Initializing");
        telemetry.update();
        bot = new Bot5446();
        bot.init(hardwareMap);
        telemetry.addData("Status", "Initialized");
        telemetry.addData("", "Press Y To Stop Robot");
        telemetry.update();
    }

    @Override
    public void init_loop()
    {

    }

    @Override
    public void start()
    {

    }

    @Override
    public void loop()
    {
        telemetry.addData("Status", "Running: " + runtime.toString());
        telemetry.addData("", "Press Y To Stop Robot");
        if (programMode == ProgramMode.MAX_TEST)
        {
            if(maxTestActive)
            {
                bot.mecanumDrive.run(1.0f,0.0f, 0.0f, false);
                positions.add(clicksToInches(bot.leftBackMotor.getCurrentPosition()));
                if (gamepad1.y)
                {
                    bot.mecanumDrive.run(0,0,0,false);
                    maxTestActive = false;
                    maxDistanceRecorded = positions.get(positions.size() - 1);
                    maxTestingTime = (float)runtime.seconds();

                    for (int i = 0; i < positions.size() - 1; i++)
                    {
                        float newVelocity = positions.get(i+1) - positions.get(i);
                        velocities.add(newVelocity);
                        if (newVelocity > maxVelocityRecorded)
                        {
                            maxVelocityRecorded = newVelocity;
                        }
                    }
                    for (int i = 0; i < velocities.size() - 1; i++)
                    {
                        float newAcceleration = velocities.get(i+1) - velocities.get(i);
                        accelerations.add(newAcceleration);
                        if (newAcceleration > maxAccelerationRecorded)
                        {
                            maxAccelerationRecorded = newAcceleration;
                        }
                    }
                }
            }
            else
            {
                bot.mecanumDrive.run(0,0,0,false);
                telemetry.addData("Test Duration", maxTestingTime);
                telemetry.addData("Traveled Distance", maxDistanceRecorded);
                telemetry.addData("Max Velocity", maxVelocityRecorded);
                telemetry.addData("Max Acceleration", maxAccelerationRecorded);
            }

        }
        telemetry.update();

    }

    @Override
    public void stop()
    {
        bot.stop();
    }

    //region inches <--> clicks
    int inchesToClicks(double inch)
    {
        return Encoder.clicksByDistance(Encoder.MotorTypes.ANDYMARK40, inch, 4.0f, 1.5f); //ANDYMARK40 has 1140 ticksPerRevolution
    }

    float clicksToInches(int clicks)
    {
        return (clicks / Encoder.clicksByInch(Encoder.MotorTypes.ANDYMARK40, 4.0f, 1.5f));
    }
    //endregion
}
