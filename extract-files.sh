#!/bin/bash
#
# SPDX-FileCopyrightText: 2016 The CyanogenMod Project
# SPDX-FileCopyrightText: 2017-2024 The LineageOS Project
# SPDX-License-Identifier: Apache-2.0
#

set -e

DEVICE=aura
VENDOR=razer

# Load extract_utils and do some sanity checks
MY_DIR="${BASH_SOURCE%/*}"
if [[ ! -d "${MY_DIR}" ]]; then MY_DIR="${PWD}"; fi

ANDROID_ROOT="${MY_DIR}/../../.."

# If XML files don't have comments before the XML header, use this flag
# Can still be used with broken XML files by using blob_fixup
export TARGET_DISABLE_XML_FIXING=true

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
        --only-firmware)
            ONLY_FIRMWARE=true
            ;;
        -n | --no-cleanup)
            CLEAN_VENDOR=false
            ;;
        -k | --kang)
            KANG="--kang"
            ;;
        -s | --section)
            SECTION="${2}"
            shift
            CLEAN_VENDOR=false
            ;;
        *)
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
        system_ext/lib64/lib-imsvideocodec.so)
            [ "$2" = "" ] && return 0
            grep -q "libgui_shim.so" "${2}" || "${PATCHELF}" --add-needed "libgui_shim.so" "${2}"
            "${PATCHELF}" --replace-needed "libqdMetaData.so" "libqdMetaData.system.so" "${2}"
            ;;
        system_ext/lib*/com.qualcomm.qti.ant@1.0.so | \
            vendor/bin/hw/android.hardware.bluetooth@1.0-service-qti | \
            vendor/bin/hw/vendor.qti.esepowermanager@1.0-service | \
            vendor/bin/hw/vendor.qti.hardware.factory@1.0-service | \
            vendor/bin/hw/vendor.qti.hardware.iop@2.0-service | \
            vendor/bin/hw/vendor.qti.hardware.qteeconnector@1.0-service | \
            vendor/bin/hw/vendor.qti.hardware.sensorscalibrate@1.0-service | \
            vendor/bin/hw/vendor.qti.hardware.tui_comm@1.0-service-qti | \
            vendor/lib*/libGPQTEEC_vendor.so | \
            vendor/lib*/libQTEEConnector_vendor.so | \
            vendor/lib*/libqti-iopd.so | \
            vendor/lib*/libqti-iopd-client.so | \
            vendor/lib*/libsecureui_svcsock.so | \
            vendor/lib*/vendor.qti.hardware.iop@1.0.so | \
            vendor/lib*/hw/vendor.qti.esepowermanager@1.0-impl.so | \
            vendor/lib*/hw/vendor.qti.hardware.factory@1.0-impl.so | \
            vendor/lib*/hw/vendor.qti.hardware.qteeconnector@1.0-impl.so)
            [ "$2" = "" ] && return 0
            "${PATCHELF}" --replace-needed "libhidlbase.so" "libhidlbase-v32.so" "${2}"
            ;;
        vendor/bin/pm-service)
            [ "$2" = "" ] && return 0
            grep -q libutils-v33.so "${2}" || "${PATCHELF}" --add-needed "libutils-v33.so" "${2}"
            ;;
        vendor/lib/hw/audio.primary.sdm845.so)
            [ "$2" = "" ] && return 0
            "${PATCHELF}" --replace-needed "libcutils.so" "libprocessgroup.so" "${2}"
            "${PATCHELF}" --replace-needed "libtinycompress_vendor.so" "libtinycompress.so" "${2}"
            ;;
        vendor/lib*/libbthost_if.so)
            [ "$2" = "" ] && return 0
            "${PATCHELF}" --set-soname "libbthost_if.so" "${2}"
            ;;
        vendor/lib*/libgps.utils.so)
            [ "$2" = "" ] && return 0
            "${PATCHELF}" --replace-needed "libcutils.so" "libprocessgroup.so" "${2}"
            ;;
        *)
            return 1
            ;;
    esac

    return 0
}

function blob_fixup_dry() {
    blob_fixup "$1" ""
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
