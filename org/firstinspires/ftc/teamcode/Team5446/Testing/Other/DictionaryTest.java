package org.firstinspires.ftc.teamcode.Team5446.Testing.Other;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "Dictionary Test", group = "Team 5446")
@Disabled
public class DictionaryTest extends LinearOpMode {

    Dictionary<String, Integer> myDictionary =  new Dictionary<String, Integer>() {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public Enumeration<String> keys() {
            return null;
        }

        @Override
        public Enumeration<Integer> elements() {
            return null;
        }

        @Override
        public Integer get(Object key) {
            return null;
        }

        @Override
        public Integer put(String key, Integer value) {
            return null;
        }

        @Override
        public Integer remove(Object key) {
            return null;
        }
    };
    Map<String, Integer> myMap1 = new Map<String, Integer>() {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean containsKey(Object key) {
            return false;
        }

        @Override
        public boolean containsValue(Object value) {
            return false;
        }

        @Override
        public Integer get(Object key) {
            return null;
        }

        @Override
        public Integer put(String key, Integer value) {
            return null;
        }

        @Override
        public Integer remove(Object key) {
            return null;
        }

        @Override
        public void putAll(Map<? extends String, ? extends Integer> m) {

        }

        @Override
        public void clear() {

        }

        @Override
        public Set<String> keySet() {
            return null;
        }

        @Override
        public Collection<Integer> values() {
            return null;
        }

        @Override
        public Set<Entry<String, Integer>> entrySet() {
            return null;
        }
    };
    HashMap<String, Integer> myMap2 = new HashMap();


    @Override
    public void runOpMode()
    {
        myDictionary.put("MyKey", 15);
        myMap1.put("MyKey", 15);
        myMap2.put("MyKey", 15);
        telemetry.addData("Dictionary 15?", myDictionary.get("MyKey")); //Null Output
        telemetry.addData("Map1 15?", myMap1.get("MyKey")); //Null Output
        telemetry.addData("Map2 15 + 2?", myMap2.get("MyKey") + 2); //Success


        telemetry.update();
        waitForStart();
    }
}
