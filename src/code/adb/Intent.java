package code.adb;


import java.util.*;

import code.util.StringUtil;

public class Intent {
    private String mAction;//-a
    private String mData;//-d
    private String mType;//-t
    private String mPackageName;
    private String mClassName;
    private int mFlags;//-f
    private HashSet<String> mCategories;//-c
    private HashMap mExtras;
    private Intent mSelector;

    public Intent(String action) {
        setAction(action);
    }

    public Intent(String aPackage, String aClassName) {
        mPackageName = aPackage;
        mClassName = aClassName;
    }


    public String getExecString() {
        StringBuilder sb = new StringBuilder();
        //设置: Action 或者 packageName,ClassName
        if (!StringUtil.isEmpty(mAction)) {
            sb.append("-a ");
            sb.append(mAction);
            sb.append(" ");
        } else if (!StringUtil.isEmpty(mPackageName) && !StringUtil.isEmpty(mClassName)) {
            sb.append("-m ");
            sb.append(mPackageName);
            sb.append(" / ");
            sb.append(mClassName);
            sb.append(" ");
        } else {
            throw new RuntimeException("");
        }
        //---------设置参数---------
        if (!StringUtil.isEmpty(mData)) {
            sb.append("-d ");
            sb.append(mData);
            sb.append(" ");
        }
        if (!StringUtil.isEmpty(mType)) {
            sb.append("-t ");
            sb.append(mType);
            sb.append(" ");
        }
        return sb.toString();
    }

    public Intent setAction(String action) {
        mAction = action != null ? action.intern() : null;
        return this;
    }

    public String getAction() {
        return mAction;
    }

    public String getData() {
        return mData;
    }

    public void setData(String mData) {
        this.mData = mData;
    }

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public void setPackageName(String mPackageName) {
        this.mPackageName = mPackageName;
    }

    public String getClassName() {
        return mClassName;
    }

    public void setClassName(String mClassName) {
        this.mClassName = mClassName;
    }

    public int getFlags() {
        return mFlags;
    }

    public void setFlags(int mFlags) {
        this.mFlags = mFlags;
    }

    public HashSet<String> getCategories() {
        return mCategories;
    }

    public void setCategories(HashSet<String> mCategories) {
        this.mCategories = mCategories;
    }

    public HashMap getExtras() {
        return mExtras;
    }

    public void setExtras(HashMap mExtras) {
        this.mExtras = mExtras;
    }

    public Intent getSelector() {
        return mSelector;
    }

    public void setSelector(Intent mSelector) {
        this.mSelector = mSelector;
    }

    public Intent setDataAndType(String data, String type) {
        mData = data;
        mType = type;
        return this;
    }

    public Intent addFlags(int flags) {
        mFlags |= flags;
        return this;
    }


}
