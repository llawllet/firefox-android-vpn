# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in ${android.home}/tools/base/proguard/proguard-android.txt
# and edit the ${android.home}/tools/base/proguard/proguard-android.txt
# file to add more flags.

# For using Jetpack Compose
# https://developer.android.com/jetpack/compose/compiler#configuration
-dontwarn androidx.compose.**
-dontkeep class androidx.compose.runtime.Composer

# Keep -Interfaces so they can be used by reflection
-keep,allowobfuscation interface * {
    *;
}

# Retain generated class which implements Parcelable.
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Keep names of classes that are referenced in the AndroidManifest.
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# Keep names of methods that are invoked via reflection from the layout files.
-keepclassmembers class * {
    @androidx.lifecycle.OnLifecycleEvent *;
}

# Keep names of methods that are invoked via reflection from the layout files.
-keepclassmembers class * {
    @androidx.core.view.ViewPropertyAnimatorListener *;
}

# Keep names of methods that are invoked via reflection from the layout files.
-keepclassmembers class * {
    @androidx.core.view.ViewPropertyAnimatorListenerUpdate *;
}

# Keep names of methods that are invoked via reflection from the layout files.
-keepclassmembers class * {
    @androidx.core.view.ViewPropertyAnimatorListener $ *;
}

# Keep names of methods that are invoked via reflection from the layout files.
-keepclassmembers class * {
    @androidx.core.view.ViewPropertyAnimatorListenerUpdate $ *;
}

# Keep names of classes that are extended by Android components.
-keepclassmembers,allowobfuscation class * extends android.app.Activity {
    public <init>(android.content.Context);
}
-keepclassmembers,allowobfuscation class * extends android.app.Application {
    public <init>();
}
-keepclassmembers,allowobfuscation class * extends android.app.Service {
    public <init>();
}
-keepclassmembers,allowobfuscation class * extends android.content.BroadcastReceiver {
    public <init>();
}
-keepclassmembers,allowobfuscation class * extends android.content.ContentProvider {
    public <init>();
}

# Keep names of classes that are referenced in the AndroidManifest.
-keepclassmembernames class * {
    android.content.Context context;
    android.app.Activity activity;
    android.app.Application application;
}