<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="evan.zhku.exp7_3"
    android:versionCode="1"
    android:versionName="1.0" >

    <uses-sdk
        android:minSdkVersion="19"
        android:targetSdkVersion="28" />

    <application
        android:allowBackup="true"
        android:appComponentFactory="android.support.v4.app.CoreComponentFactory"
        android:debuggable="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:testOnly="true"
        android:theme="@style/AppTheme" >
        <activity android:name="evan.zhku.exp7_3.MainActivity" >
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <provider
            android:name="com.android.tools.ir.server.InstantRunContentProvider"
            android:authorities="evan.zhku.exp7_3.com.android.tools.ir.server.InstantRunContentProvider"
            android:multiprocess="true" />
    </application>

</manifest>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         