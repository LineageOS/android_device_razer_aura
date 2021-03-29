#!/bin/bash
#
# Copyright (C) 2018-2020 The LineageOS Project
#
# SPDX-License-Identifier: Apache-2.0
#

set -e

DEVICE=aura
VENDOR=razer

# Load extract_utils and do some sanity checks
MY_DIR="${BASH_SOURCE%/*}"
if [[ ! -d "${MY_DIR}" ]]; then MY_DIR="${PWD}"; fi

LINEAGE_ROOT="${MY_DIR}"/../../..

HELPER="${LINEAGE_ROOT}/vendor/lineage/build/tools/extract_utils.sh"
if [ ! -f "${HELPER}" ]; then
    echo "Unable to find helper script at ${HELPER}"
    exit 1
fi
source "${HELPER}"

function blob_fixup() {
    case "${1}" in
    product/etc/permissions/telephonyservice.xml)
        sed -i 's|/system/framework/QtiTelephonyServicelibrary.jar|/product/framework/QtiTelephonyServicelibrary.jar|g' "${2}"
        ;;
    vendor/etc/gpfspath_oem_config.xml)
        sed -i 's|/vendor/securefs/data/|/mnt/vendor/securefs/data/|g' "${2}"
        ;;
    vendor/lib/hw/audio.primary.sdm845.so)
        patchelf --replace-needed "libcutils.so" "libprocessgroup.so" "${2}"
        ;;
    vendor/lib64/hw/audio.primary.sdm845.so)
        patchelf --replace-needed "libcutils.so" "libprocessgroup.so" "${2}"
        ;;
    vendor/lib/libgps.utils.so)
        patchelf --replace-needed "libcutils.so" "libprocessgroup.so" "${2}"
        ;;
    vendor/lib64/libgps.utils.so)
        patchelf --replace-needed "libcutils.so" "libprocessgroup.so" "${2}"
        ;;
    esac
}

# Default to sanitizing the vendor folder before extraction
CLEAN_VENDOR=true

SECTION=
KANG=

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

# Initialize the helper
setup_vendor "${DEVICE}" "${VENDOR}" "${LINEAGE_ROOT}" false "${CLEAN_VENDOR}"

extract "${MY_DIR}/proprietary-files.txt" "${SRC}" \
        "${KANG}" --section "${SECTION}"

"${MY_DIR}/setup-makefiles.sh"
