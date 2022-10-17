//
// Created by tiago on 13/10/2022.
//

#include <string.h>
#include <stdlib.h>
#include <jni.h>


jstring  Java_tiago_cognizant_reexercise2_MainActivity_getElfString(JNIEnv * env, jobject obj){

    return (*env)->NewStringUTF(env, "ELF_STRING_y");
}

jstring  Java_tiago_cognizant_reexercise2_MainActivity_nativeDecryptString(JNIEnv * env, jobject obj, jstring string){
    char* secret = "mysecret";
    const char* stringToEncrypt = (*env)->GetStringUTFChars(env,string, 0);
    //strcpy(stringToEncrypt,string);
    char* s = malloc(strlen(stringToEncrypt));


    for (int i = 0; i < strlen(stringToEncrypt); ++i) {
        int code1 = (int) stringToEncrypt[i];
        int code2 = (int) secret[i% strlen(secret)];
        int res = code1 ^ code2;
        s[i] = (char) res;
    }

    //free((void*)stringToEncrypt);
    return (*env)->NewStringUTF(env, s);
}