//
// Created by tiago on 13/10/2022.
//

#include <string.h>
#include <jni.h>

jstring  Java_tiago_cognizant_reexercise2_MainActivity_sayWelcome(JNIEnv * env, jobject obj ){

    return (*env)->NewStringUTF(env, "Welcome");
}