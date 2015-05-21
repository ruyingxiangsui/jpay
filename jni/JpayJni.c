#include<stdio.h>
#include<sys/stat.h>
#include<unistd.h>
#include<fcntl.h>
#include <errno.h>
#include"JpayJni.h"

/*
 *16字节
 *
 * */
const char keyValue[] = { 21, 33, 21, -45, 25, 98, -55, -45, 10, 35, -45, 35,
		26, -5, 25, -65, };

const char iv[] = { -33, 32, -25, 25, 35, -27, 55, -12, -15, 32, 23, 45, -26,
		32, 5, 16 };
//1532883504
//-1642232357
const int jpay_signature_hash = -1642232357;
/*
 * Class:     application_JPayApplication
 * Method:    getKey
 * Signature: ()[B
 */
JNIEXPORT jbyteArray JNICALL Java_application_JPayApplication_getKey(
		JNIEnv *env, jobject obj) {
	if (!checkSignature(env, obj)) {
		return NULL;
	}
	jbyteArray kvArray = (*env)->NewByteArray(env, sizeof(keyValue));
	jbyte *bytes = (*env)->GetByteArrayElements(env, kvArray, 0);

	int i;
	for (i = 0; i < sizeof(keyValue); i++) {
		bytes[i] = (jbyte) keyValue[i];
	}

	(*env)->SetByteArrayRegion(env, kvArray, 0, sizeof(keyValue), bytes);
	(*env)->ReleaseByteArrayElements(env, kvArray, bytes, 0);

	return kvArray;
}

/*
 * Class:     application_JPayApplication
 * Method:    getIv
 * Signature: ()[B
 */
JNIEXPORT jbyteArray JNICALL Java_application_JPayApplication_getIv(JNIEnv *env,
		jobject obj) {
	if (!checkSignature(env, obj)) {
		return NULL;
	}
	jbyteArray ivArray = (*env)->NewByteArray(env, sizeof(iv));
	jbyte *bytes = (*env)->GetByteArrayElements(env, ivArray, 0);

	int i;
	for (i = 0; i < sizeof(iv); i++) {
		bytes[i] = (jbyte) iv[i];
	}

	(*env)->SetByteArrayRegion(env, ivArray, 0, sizeof(iv), bytes);
	(*env)->ReleaseByteArrayElements(env, ivArray, bytes, 0);

	return ivArray;
}



JNIEXPORT jstring JNICALL Java_application_JPayApplication_getCtime(JNIEnv *env,
		jobject thiz, jstring file_path) {
	struct stat buf;
	//"/data/data/com.yunhuirong.jpayapp/files/q"

	char* s = (*env)->GetStringUTFChars(env, file_path, 0);
	int res = stat(s, &buf);
	if (res == 0) {
		char tmp[32];
		sprintf(tmp,"%ld",buf.st_ctime);
		return (*env)->NewStringUTF(env, tmp);
	} else {
		return NULL;
	}

}

int checkSignature(JNIEnv *env, jobject thiz) {
	// 获得 Context 类
	jclass native_clazz = (*env)->GetObjectClass(env, thiz);

	// 得到 getPackageManager 方法的 ID
	jmethodID methodID_func = (*env)->GetMethodID(env, native_clazz,
			"getPackageManager", "()Landroid/content/pm/PackageManager;");

	// 获得应用包的管理器
	jobject package_manager = (*env)->CallObjectMethod(env, thiz,
			methodID_func);
	// 获得 PackageManager 类
	jclass pm_clazz = (*env)->GetObjectClass(env, package_manager);

	// 得到 getPackageInfo 方法的 ID
	jmethodID methodID_pm = (*env)->GetMethodID(env, pm_clazz, "getPackageInfo",
			"(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");

	// 获得应用包的信息
	jobject package_info = (*env)->CallObjectMethod(env, package_manager,
			methodID_pm, (*env)->NewStringUTF(env, "com.yunhuirong.jpayapp"),
			64);

	// 获得 PackageInfo 类
	jclass pi_clazz = (*env)->GetObjectClass(env, package_info);

	// 获得签名数组属性的 ID
	jfieldID fieldID_signatures = (*env)->GetFieldID(env, pi_clazz,
			"signatures", "[Landroid/content/pm/Signature;");

	// 得到签名数组，待修改
	jobjectArray signatures = (*env)->GetObjectField(env, package_info,
			fieldID_signatures);

	// 得到签名
	jobject signature = (*env)->GetObjectArrayElement(env, signatures, 0);

	// 获得 Signature 类，待修改
	jclass s_clazz = (*env)->GetObjectClass(env, signature);

	// 得到 hashCode 方法的 ID
	jmethodID methodID_hc = (*env)->GetMethodID(env, s_clazz, "hashCode",
			"()I");

	// 获得签名的hash值
	int hash_code = (*env)->CallIntMethod(env, signature, methodID_hc);

	if (hash_code == jpay_signature_hash) {
		return 1;
	}
	return 0;
}

