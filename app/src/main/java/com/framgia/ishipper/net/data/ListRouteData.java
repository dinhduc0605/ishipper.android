package com.framgia.ishipper.net.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by dinhduc on 09/11/2016.
 */

public class ListRouteData {
    @SerializedName("routes")
    private ArrayList<Route> mRoutes;

    public ArrayList<Route> getRoutes() {
        return mRoutes;
    }

    public class Route {
        @SerializedName("legs")
        private ArrayList<Leg> mLegs;

        public ArrayList<Leg> getLegs() {
            return mLegs;
        }
    }

    public class Leg {
        @SerializedName("steps")
        private ArrayList<Step> mSteps;

        public ArrayList<Step> getSteps() {
            return mSteps;
        }
    }

    public class Step {
        @SerializedName("html_instructions")
        private String htmlInstructions;
        @SerializedName("distance")
        private Distance mDistance;

        public String getHtmlInstructions() {
            return htmlInstructions;
        }

        public Distance getDistance() {
            return mDistance;
        }
    }

    public class Distance {
        @SerializedName("text")
        private String mText;
        @SerializedName("value")
        private int mValue;

        public String getText() {
            return mText;
        }

        public int getValue() {
            return mValue;
        }
    }

}
