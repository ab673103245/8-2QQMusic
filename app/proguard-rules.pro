# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Program\sdk\android-sdk-windows/tools/proguard/proguard-android.txt
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
#
## 指定混淆级别
#-optimizationpasses 5
#
##保持哪些类不被混淆
#-keep class com.squareup.** {*;}
#-keep class okio.** {*;}
#-keep class okhttp3.**{*;}
#
##忽略警告
#-dontwarn com.squareup.**
#-dontwarn okio.**
#-dontwarn okhttp3.**
