package com.bennelson.familymap.Model_Classes;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by BenNelson on 12/7/16.
 */

public class FamilyLines
{
    private LatLng pointA;
    private LatLng pointB;
    private int genCount;

    public FamilyLines (LatLng pointA, LatLng pointB)
    {
        this.pointA = pointA;
        this.pointB = pointB;
    }

    public LatLng getPointA() {
        return pointA;
    }

    public void setPointA(LatLng pointA) {
        this.pointA = pointA;
    }

    public LatLng getPointB() {
        return pointB;
    }

    public void setPointB(LatLng pointB) {
        this.pointB = pointB;
    }

    public int getGenCount() {
        return genCount;
    }

    public void setGenCount(int genCount) {
        this.genCount = genCount;
    }
}
