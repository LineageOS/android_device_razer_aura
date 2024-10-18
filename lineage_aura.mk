#
# Copyright (C) 2018-2020 The LineageOS Project
#
# SPDX-License-Identifier: Apache-2.0
#

$(call inherit-product, device/razer/aura/device.mk)

# Inherit some common Lineage stuff.
$(call inherit-product, vendor/lineage/config/common_full_phone.mk)

# Device identifier. This must come after all inclusions.
PRODUCT_NAME := lineage_aura
PRODUCT_DEVICE := aura
PRODUCT_BRAND := razer
PRODUCT_MODEL := Phone 2
PRODUCT_MANUFACTURER := Razer

BUILD_FINGERPRINT := "razer/cheryl2/aura:9/P-SMR7-RC003-RZR-210107/3225:user/release-keys"

PRODUCT_BUILD_PROP_OVERRIDES += \
    BuildDesc="aura-user 9 P-SMR7-RC003-RZR-210107 3225 release-keys" \
    BuildFingerprint=razer/cheryl2/aura:9/P-SMR7-RC003-RZR-210107/3225:user/release-keys \
    DeviceName=cheryl2

PRODUCT_GMS_CLIENTID_BASE := android-razer
