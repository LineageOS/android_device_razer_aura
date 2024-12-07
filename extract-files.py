#!/usr/bin/env -S PYTHONPATH=../../../tools/extract-utils python3
#
# SPDX-FileCopyrightText: 2024 The LineageOS Project
# SPDX-License-Identifier: Apache-2.0
#

import extract_utils.tools

extract_utils.tools.DEFAULT_PATCHELF_VERSION = '0_9'

from extract_utils.fixups_blob import (
    blob_fixup,
    blob_fixups_user_type,
)
from extract_utils.fixups_lib import (
    lib_fixup_remove,
    lib_fixups,
    lib_fixups_user_type,
)
from extract_utils.main import (
    ExtractUtils,
    ExtractUtilsModule,
)

namespace_imports = [
    'device/razer/aura',
    'hardware/qcom-caf/sdm845',
    'hardware/qcom-caf/wlan',
    'vendor/qcom/opensource/commonsys-intf/display',
    'vendor/qcom/opensource/commonsys/display',
    'vendor/qcom/opensource/dataservices',
    'vendor/qcom/opensource/display',
]

def lib_fixup_vendor_suffix(lib: str, partition: str, *args, **kwargs):
    return f'{lib}_{partition}' if partition == 'vendor' else None

lib_fixups: lib_fixups_user_type = {
    **lib_fixups,
    (
        'com.qualcomm.qti.ant@1.0',
        'com.qualcomm.qti.dpm.api@1.0',
        'libmmosal',
        'vendor.qti.imsrtpservice@3.0',
        'vendor.qti.hardware.wifidisplaysession@1.0',
    ): lib_fixup_vendor_suffix,
    (
        'libOmxCore',
        'libril',
        'libwpa_client',
    ): lib_fixup_remove,
}

blob_fixups: blob_fixups_user_type = {
    (
        'system_ext/lib/com.qualcomm.qti.ant@1.0.so',
        'system_ext/lib64/com.qualcomm.qti.ant@1.0.so',
        'vendor/bin/hw/android.hardware.bluetooth@1.0-service-qti',
        'vendor/bin/hw/vendor.qti.esepowermanager@1.0-service',
        'vendor/bin/hw/vendor.qti.hardware.factory@1.0-service',
        'vendor/bin/hw/vendor.qti.hardware.iop@2.0-service',
        'vendor/bin/hw/vendor.qti.hardware.qteeconnector@1.0-service',
        'vendor/bin/hw/vendor.qti.hardware.sensorscalibrate@1.0-service',
        'vendor/bin/hw/vendor.qti.hardware.tui_comm@1.0-service-qti',
        'vendor/lib/com.qualcomm.qti.ant@1.0.so',
        'vendor/lib/libGPQTEEC_vendor.so',
        'vendor/lib/libQTEEConnector_vendor.so',
        'vendor/lib/libqti-iopd.so',
        'vendor/lib/libqti-iopd-client.so',
        'vendor/lib/libsecureui_svcsock.so',
        'vendor/lib/vendor.qti.esepowermanager@1.0.so',
        'vendor/lib/vendor.qti.hardware.factory@1.0.so',
        'vendor/lib/vendor.qti.hardware.iop@1.0.so',
        'vendor/lib/vendor.qti.hardware.iop@2.0.so',
        'vendor/lib/vendor.qti.hardware.qteeconnector@1.0.so',
        'vendor/lib/vendor.qti.hardware.scve.objecttracker@1.0.so',
        'vendor/lib/vendor.qti.hardware.scve.panorama@1.0.so',
        'vendor/lib/vendor.qti.hardware.sensorscalibrate@1.0.so',
        'vendor/lib/vendor.qti.hardware.tui_comm@1.0.so',
        'vendor/lib/hw/vendor.qti.esepowermanager@1.0-impl.so',
        'vendor/lib/hw/vendor.qti.hardware.factory@1.0-impl.so',
        'vendor/lib/hw/vendor.qti.hardware.qteeconnector@1.0-impl.so',
        'vendor/lib64/com.qualcomm.qti.ant@1.0.so',
        'vendor/lib64/libGPQTEEC_vendor.so',
        'vendor/lib64/libQTEEConnector_vendor.so',
        'vendor/lib64/libqti-iopd.so',
        'vendor/lib64/libqti-iopd-client.so',
        'vendor/lib64/libsecureui_svcsock.so',
        'vendor/lib64/vendor.qti.esepowermanager@1.0.so',
        'vendor/lib64/vendor.qti.hardware.factory@1.0.so',
        'vendor/lib64/vendor.qti.hardware.iop@1.0.so',
        'vendor/lib64/vendor.qti.hardware.iop@2.0.so',
        'vendor/lib64/vendor.qti.hardware.qteeconnector@1.0.so',
        'vendor/lib64/vendor.qti.hardware.scve.objecttracker@1.0.so',
        'vendor/lib64/vendor.qti.hardware.scve.panorama@1.0.so',
        'vendor/lib64/vendor.qti.hardware.sensorscalibrate@1.0.so',
        'vendor/lib64/vendor.qti.hardware.tui_comm@1.0.so',
        'vendor/lib64/hw/vendor.qti.esepowermanager@1.0-impl.so',
        'vendor/lib64/hw/vendor.qti.hardware.factory@1.0-impl.so',
        'vendor/lib64/hw/vendor.qti.hardware.qteeconnector@1.0-impl.so',
    ): blob_fixup()
        .replace_needed('libhidlbase.so', 'libhidlbase-v32.so'),
    'system_ext/lib/libwfdmmsrc_system.so': blob_fixup()
        .add_needed('libgui_shim.so'),
    'system_ext/lib64/libwfdnative.so': blob_fixup()
        .add_needed('libinput_shim.so'),
    'system_ext/lib/libwfdservice.so': blob_fixup()
        .replace_needed('android.media.audio.common.types-V2-cpp.so', 'android.media.audio.common.types-V4-cpp.so'),
    ('system_ext/lib64/lib-imscamera.so', 'system_ext/lib64/lib-imsvideocodec.so'): blob_fixup()
        .add_needed('libgui_shim.so')
        .replace_needed('libqdMetaData.so', 'libqdMetaData.system.so'),
    'vendor/bin/hw/android.hardware.drm@1.1-service.widevine': blob_fixup()
        .replace_needed('libhidltransport.so', 'libhidlbase.so')
        .remove_needed('libhwbinder.so'),
    'vendor/lib/hw/audio.primary.sdm845.so': blob_fixup()
        .add_needed('libprocessgroup.so')
        .replace_needed('libtinycompress_vendor.so', 'libtinycompress.so'),
    ('vendor/lib/libbthost_if.so', 'vendor/lib64/libbthost_if.so'): blob_fixup()
        .fix_soname(),
    ('vendor/lib/libgps.utils.so', 'vendor/lib64/libgps.utils.so'): blob_fixup()
        .replace_needed('libcutils.so', 'libprocessgroup.so'),
    ('vendor/lib64/libwvhidl.so', 'vendor/lib/mediadrm/libwvdrmengine.so', 'vendor/lib64/mediadrm/libwvdrmengine.so'): blob_fixup()
        .add_needed('libcrypto_shim.so'),
}  # fmt: skip

module = ExtractUtilsModule(
    'aura',
    'razer',
    blob_fixups=blob_fixups,
    lib_fixups=lib_fixups,
    namespace_imports=namespace_imports,
    add_firmware_proprietary_file=True,
)

if __name__ == '__main__':
    utils = ExtractUtils.device(module)
    utils.run()
