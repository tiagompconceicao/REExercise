#include <string.h>
#include <stdlib.h>
#include <jni.h>

int invs[] = {62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58,
              59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5,
              6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
              21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28,
              29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42,
              43, 44, 45, 46, 47, 48, 49, 50, 51 };

size_t decoded_size(const char *in) {
    size_t len;
    size_t ret;
    size_t i;

    if (in == NULL)
        return 0;

    len = strlen(in);
    ret = len / 4 * 3;

    for (i=len; i-->0; ) {
        if (in[i] == '=') {
            ret--;
        } else {
            break;
        }
    }

    return ret;
}

int decode(const char *in, unsigned char *out) {
    size_t len;
    size_t i;
    size_t j;
    int    v;

    if (in == NULL || out == NULL)
        return 0;

    len = strlen(in);

    for (i=0, j=0; i<len; i+=4, j+=3) {
        v = invs[in[i] - 43];
        v = (v << 6) | invs[in[i + 1] - 43];
        v = in[i+2]=='=' ? v << 6 : (v << 6) | invs[in[i + 2] - 43];
        v = in[i+3]=='=' ? v << 6 : (v << 6) | invs[in[i + 3] - 43];

        out[j] = (v >> 16) & 0xFF;
        if (in[i+2] != '=')
            out[j+1] = (v >> 8) & 0xFF;
        if (in[i+3] != '=')
            out[j+2] = v & 0xFF;
    }

    return 1;
}

jstring  Java_tiago_cognizant_reexercise2_MainActivity_getString(JNIEnv * env, jobject obj){

    return (*env)->NewStringUTF(env, "ELF_CTF_y");
}

jstring  Java_tiago_cognizant_reexercise2_MainActivity_get(JNIEnv * env, jobject obj, jstring string){
    const char * enc = (*env)->GetStringUTFChars(env,string, 0);
    size_t out_len = decoded_size(enc)+1;
    char * out = malloc(out_len);

    decode(enc, (unsigned char *) out);
    out[out_len] = '\0';

    return (*env)->NewStringUTF(env, out);
}