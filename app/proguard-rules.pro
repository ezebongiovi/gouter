# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/epasquale/Library/Android/sdk/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-dontwarn javax.annotation.**
-verbose
-dump class_files.txt
-printseeds seeds.txt
-printusage unused.txt
-printmapping mapping.txt

-keepnames class com.testableapp.dto.** { *; }

##############++++++ Support libraries ++++++###############
-keep class android.support.multidex.MultiDex { *; }

####################++++++ Annotations ++++++##########################
#-keepclassmembers class * implements java.lang.annotation.Annotation {
#    ** *();
#}
#-dontwarn javax.annotation.**
####################------ Annotations ------##########################
-keep class * implements java.lang.annotation.Annotation { *; }
-keep class javax.annotation.* { *; }

###################–------SQUARE------########################
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**

###################------ GSON ------##########################
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

###################------ Stepper ------##########################
-keep class com.stepstone.stepper.** { *; }

###################------ Retrofit ------##########################
-keep class retrofit2.Response { *; }
-keep class okhttp3.** { *; }
-keep class okio.** { *; }

-dontwarn retrofit2.**
-dontwarn okio.**

#### Google #####
-keep class com.google.android.** { *; }

#### Data Binding ####
-keep class android.databinding.BaseObservable { *; }

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class sun.security.ssl.** { *; }
#-keep class com.google.gson.stream.** { *; }