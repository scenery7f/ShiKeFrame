# ShiKeFrame
fast frame

Gradle:

Step 1. Add the JitPack repository to your build file

    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }

Step 2. Add the dependency

    dependencies {
            implementation 'com.github.scenery7f:ShiKeFrame:v1.4'
    }

    defaultConfig {
        ...
        javaCompileOptions {
            annotationProcessorOptions {
                includeCompileClasspath true
            }
        }
        packagingOptions{
            doNotStrip '*/mips/*.so'
            doNotStrip '*/mips64/*.so'
        }...
    }
