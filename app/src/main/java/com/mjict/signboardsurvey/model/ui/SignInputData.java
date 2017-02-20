package com.mjict.signboardsurvey.model.ui;

/**
 * Created by Junseo on 2017-02-13.
 */
public class SignInputData {
    public String signImagePath;
    public String content;
    public Object signType;
    public Object signStats;
    public String width;
    public String length;
    public Object lightType;
    public String placedFloor;
    public String totalFloor;
    public String height;
    public boolean isFront;
    public boolean isIntersection;
    public boolean isFrontBack;
    public boolean isCollision;
    public String collisionWidth;
    public String collisionLength;
    public Object reviewStats;
    public Object installSide;
    public Object uniqueness;
    public String memo;
    public String demolishImagePath;
    public String demolishDate;
    public Object inspectionResult;

    public SignInputData(String signImagePath, String content, Object signType, Object signStats,
                         String width, String length, Object lightType, String placedFloor, String totalFloor,
                         String height, boolean isFront, boolean isIntersection, boolean isFrontBack,
                         boolean isCollision, String collisionWidth, String collisionLength, Object reviewStats,
                         Object installSide, Object uniqueness, String memo, String demolishImagePath,
                         String demolishDate, Object inspectionResult) {
        this.signImagePath = signImagePath;
        this.content = content;
        this.signType = signType;
        this.signStats = signStats;
        this.width = width;
        this.length = length;
        this.lightType = lightType;
        this.placedFloor = placedFloor;
        this.totalFloor = totalFloor;
        this.height = height;
        this.isFront = isFront;
        this.isIntersection = isIntersection;
        this.isFrontBack = isFrontBack;
        this.isCollision = isCollision;
        this.collisionWidth = collisionWidth;
        this.collisionLength = collisionLength;
        this.reviewStats = reviewStats;
        this.installSide = installSide;
        this.uniqueness = uniqueness;
        this.memo = memo;
        this.demolishImagePath = demolishImagePath;
        this.demolishDate = demolishDate;
        this.inspectionResult = inspectionResult;
    }

    public SignInputData() {
        signImagePath = "";
        content = "";
        signType = null;
        this.signStats = null;
        this.width = "";
        this.length = "";
        this.lightType = null;
        this.placedFloor = "";
        this.totalFloor = "";
        this.height = "";
        this.isFront = false;
        this.isIntersection = false;
        this.isFrontBack = false;
        this.isCollision = false;
        this.collisionWidth = "";
        this.collisionLength = "";
        this.reviewStats = null;
        this.installSide = null;
        this.uniqueness = null;
        this.memo = "";
        this.demolishImagePath = "";
        this.demolishDate = "";
        this.inspectionResult = "";
    }
}
