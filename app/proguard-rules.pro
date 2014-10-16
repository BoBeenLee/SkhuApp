# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Program Files (x86)\Android\android-studio\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keep class com.skhu.bobinlee.skhuapp.util.BeanUtils
-dontwarn com.fasterxml.**
-dontwarn org.joda.**
-dontwarn android.support.**

-keep class  org.joda.** {*;}
-keep class  com.fasterxml.** {*;}
-keep class  com.google.** {*;}
-keep class  com.loopj.** {*;}
-keep class  com.handmark.** {*;}
-keep class  org.apache.** {*;}
-keep class  org.json.** {*;}

-keepattributes Signature,*Annotation*,EnclosingMethod