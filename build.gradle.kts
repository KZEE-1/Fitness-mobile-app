buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
        classpath ("com.google.relay:relay-gradle-plugin:0.3.09")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.relay") version "0.3.00"

}

