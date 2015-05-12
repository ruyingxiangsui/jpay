LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE    := JpayJni
LOCAL_SRC_FILES := JpayJni.c
include $(BUILD_SHARED_LIBRARY)