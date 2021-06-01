#!/bin/bash
#
# Copyright (C) 2016 The CyanogenMod Project
# Copyright (C) 2017-2020 The LineageOS Project
#
# SPDX-License-Identifier: Apache-2.0
#

set -e

DEVICE=aura
VENDOR=razer

# Load extract_utils and do some sanity checks
MY_DIR="${BASH_SOURCE%/*}"
if [[ ! -d "${MY_DIR}" ]]; then MY_DIR="${PWD}"; fi

ANDROID_ROOT="${MY_DIR}/../../.."

HELPER="${ANDROID_ROOT}/tools/extract-utils/extract_utils.sh"
if [ ! -f "${HELPER}" ]; then
    echo "Unable to find helper script at ${HELPER}"
    exit 1
fi
source "${HELPER}"

# Default to sanitizing the vendor folder before extraction
CLEAN_VENDOR=true

KANG=
SECTION=

while [ "${#}" -gt 0 ]; do
    case "${1}" in
        -n | --no-cleanup )
                CLEAN_VENDOR=false
                ;;
        -k | --kang )
                KANG="--kang"
                ;;
        -s | --section )
                SECTION="${2}"; shift
                CLEAN_VENDOR=false
                ;;
        * )
                SRC="${1}"
                ;;
    esac
    shift
done

if [ -z "${SRC}" ]; then
    SRC="adb"
fi

function blob_fixup() {
    case "${1}" in
        system_ext/etc/init/dpmd.rc)
            sed -i "s|/system/product/bin/|/system/system_ext/bin/|g" "${2}"
            ;;
        system_ext/etc/permissions/com.qti.dpmframework.xml)
            ;&
        system_ext/etc/permissions/dpmapi.xml)
            ;&
        system_ext/etc/permissions/qcrilhook.xml)
            sed -i "s|/product/framework/|/system_ext/framework/|g" "${2}"
            ;;
        system_ext/etc/permissions/qti_libpermissions.xml)
            sed -i "s/name=\"android.hidl.manager-V1.0-java/name=\"android.hidl.manager@1.0-java/g" "${2}"
            ;;
        system_ext/etc/permissions/telephonyservice.xml)
            sed -i 's|/system/framework/|/system_ext/framework/|g' "${2}"
            ;;
        system_ext/lib64/lib-imsvideocodec.so)
            for LIBUI_SHIM in $(grep -L "libui_shim.so" "${2}"); do
                "${PATCHELF}" --add-needed "libui_shim.so" "$LIBUI_SHIM"
            done
            ;;
        system_ext/lib64/libdpmframework.so)
            sed -i "s/libhidltransport.so/libcutils-v29.so\x00\x00\x00/" "${2}"
            ;;
        vendor/lib/hw/audio.primary.sdm845.so)
            "${PATCHELF}" --replace-needed "libcutils.so" "libprocessgroup.so" "${2}"
            "${PATCHELF}" --replace-needed "libtinycompress_vendor.so" "libtinycompress.so" "${2}"
            ;;
        vendor/lib/libbthost_if.so)
            "${PATCHELF}" --set-soname "libbthost_if.so" "${2}"
            ;;
        vendor/lib/libgps.utils.so)
            "${PATCHELF}" --replace-needed "libcutils.so" "libprocessgroup.so" "${2}"
            ;;
        vendor/lib64/libbthost_if.so)
            "${PATCHELF}" --set-soname "libbthost_if.so" "${2}"
            ;;
        vendor/lib64/libgps.utils.so)
            "${PATCHELF}" --replace-needed "libcutils.so" "libprocessgroup.so" "${2}"
            ;;
    esac
}

# Initialize the helper
setup_vendor "${DEVICE}" "${VENDOR}" "${ANDROID_ROOT}" false "${CLEAN_VENDOR}"

extract "${MY_DIR}/proprietary-files.txt" "${SRC}" "${KANG}" --section "${SECTION}"

"${MY_DIR}/setup-makefiles.sh"
