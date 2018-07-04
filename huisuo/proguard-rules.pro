# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\ASUS\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
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
-keep public class com.tencent.bugly.**{*;}

   -keepattributes Signature
    -keepattributes *Annotation*
    -keep class com.mobvista.** {*; }
    -keep interface com.mobvista.** {*; }
    -keep class android.support.v4.** { *; }
    -dontwarn com.mobvista.**
    -keep class **.R$* { public static final int mobvista*; }
    -keep class com.alphab.** {*; }
    -keep interface com.alphab.** {*; }