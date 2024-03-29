#!/bin/bash
#
# Copyright (C) 2016 The CyanogenMod Project
# Copyright (C) 2017-2023 The LineageOS Project
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

ONLY_FIRMWARE=
KANG=
SECTION=

while [ "${#}" -gt 0 ]; do
    case "${1}" in
        --only-firmware )
                ONLY_FIRMWARE=true
                ;;
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
            for LIBGUI_SHIM in $(grep -L "libgui_shim.so" "${2}"); do
                "${PATCHELF}" --add-needed "libgui_shim.so" "${LIBGUI_SHIM}"
            done
            for LIBUI_SHIM in $(grep -L "libui_shim.so" "${2}"); do
                "${PATCHELF}" --add-needed "libui_shim.so" "$LIBUI_SHIM"
            done
            ;;
        system_ext/lib*/com.qualcomm.qti.ant@1.0.so|system_ext/lib64/lib-imsvt.so)
            "${PATCHELF}" --replace-needed "libhidlbase.so" "libhidlbase-v32.so" "${2}"
            ;;
        system_ext/lib64/libdpmframework.so)
            sed -i "s/libhidltransport.so/libcutils-v29.so\x00\x00\x00/" "${2}"
            ;;
        vendor/bin/hw/qcrild|vendor/bin/imsdatadaemon)
            grep -q libhidlbase-v32.so "${2}" || "${PATCHELF}" --add-needed "libhidlbase-v32.so" "${2}"
            ;;
        vendor/bin/hw/android.hardware.bluetooth@1.0-service-qti|vendor/bin/ATFWD-daemon|vendor/bin/ims_rtp_daemon|vendor/bin/netmgrd|vendor/bin/imsrcsd|vendor/bin/hw/vendor.qti.hardware.sensorscalibrate@1.0-service|vendor/bin/hw/vendor.qti.hardware.iop@2.0-service|vendor/bin/hw/vendor.qti.hardware.factory@1.0-service|vendor/bin/hw/vendor.qti.hardware.tui_comm@1.0-service-qti|vendor/bin/hw/vendor.qti.hardware.qteeconnector@1.0-service|vendor/bin/hw/vendor.qti.esepowermanager@1.0-service)
            "${PATCHELF}" --replace-needed "libhidlbase.so" "libhidlbase-v32.so" "${2}"
            ;;
        vendor/bin/pm-service)
            grep -q libutils-v33.so "${2}" || "${PATCHELF}" --add-needed "libutils-v33.so" "${2}"
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
        vendor/lib64/libcne.so|vendor/lib64/libril-qc-qmi-1.so|vendor/lib*/vendor.qti.hardware.iop@1.0.so|vendor/lib*/libqti-iopd.so|vendor/lib*/libqti-iopd-client.so|vendor/lib*/hw/vendor.qti.hardware.factory@1.0-impl.so|vendor/lib*/libsecureui_svcsock.so|vendor/lib*/libQTEEConnector_vendor.so|vendor/lib*/libGPQTEEC_vendor.so|vendor/lib*/hw/vendor.qti.hardware.qteeconnector@1.0-impl.so|vendor/lib*/hw/vendor.qti.esepowermanager@1.0-impl.so|vendor/lib64/vendor.qti.hardware.radio.am@1.0.so|vendor/lib64/vendor.qti.hardware.radio.ims@1.*.so|vendor/lib64/vendor.qti.hardware.radio.lpa@1.0.so|vendor/lib64/vendor.qti.hardware.radio.qcrilhook@1.0.so|vendor/lib64/vendor.qti.hardware.radio.qtiradio@*.*.so|vendor/lib64/vendor.qti.hardware.radio.uim@1.*.so|vendor/lib64/vendor.qti.hardware.radio.uim_remote_*@1.0.so)
            "${PATCHELF}" --replace-needed "libhidlbase.so" "libhidlbase-v32.so" "${2}"
            ;;
    esac
}

# Initialize the helper
setup_vendor "${DEVICE}" "${VENDOR}" "${ANDROID_ROOT}" false "${CLEAN_VENDOR}"

if [ -z "${ONLY_FIRMWARE}" ]; then
    extract "${MY_DIR}/proprietary-files.txt" "${SRC}" "${KANG}" --section "${SECTION}"
fi

if [ -z "${SECTION}" ]; then
    extract_firmware "${MY_DIR}/proprietary-firmware.txt" "${SRC}"
fi

"${MY_DIR}/setup-makefiles.sh"
