# Audio
PRODUCT_PROPERTY_OVERRIDES += \
    af.fast_track_multiplier=1 \
    audio.deep_buffer.media=true \
    persist.vendor.audio.fluence.speaker=true \
    persist.vendor.audio.fluence.voicecall=true \
    persist.vendor.audio.fluence.voicerec=false \
    persist.vendor.audio.ras.enabled=false \
    ro.af.client_heap_size_kbyte=7168 \
    ro.config.media_vol_steps=25 \
    ro.config.vc_call_vol_steps=11 \
    ro.vendor.audio.sdk.fluencetype=fluencepro \
    ro.vendor.audio.sdk.ssr=false \
    vendor.audio.adm.buffering.ms=2 \
    vendor.audio.dolby.ds2.enabled=false \
    vendor.audio.dolby.ds2.hardbypass=false \
    vendor.audio.enable.dp.for.voice=false \
    vendor.audio.flac.sw.decoder.24bit=true \
    vendor.audio_hal.in_period_size=144 \
    vendor.audio.hal.output.suspend.supported=true \
    vendor.audio_hal.period_size=192 \
    vendor.audio_hal.period_multiplier=3 \
    vendor.audio.hw.aac.encoder=true \
    vendor.audio.noisy.broadcast.delay=600 \
    vendor.audio.offload.buffer.size.kb=32 \
    vendor.audio.offload.gapless.enabled=true \
    vendor.audio.offload.multiaac.enable=true \
    vendor.audio.offload.multiple.enabled=false \
    vendor.audio.offload.passthrough=false \
    vendor.audio.offload.pstimeout.secs=3 \
    vendor.audio.offload.track.enable=true \
    vendor.audio.parser.ip.buffer.size=262144 \
    vendor.voice.path.for.pcm.voip=true \
    vendor.audio.safx.pbe.enabled=true \
    vendor.audio.tunnel.encode=false \
    vendor.audio.use.sw.alac.decoder=true \
    vendor.audio.use.sw.ape.decoder=true

# Bluetooth
PRODUCT_PROPERTY_OVERRIDES += \
    vendor.qcom.bluetooth.soc=cherokee

# Camera
PRODUCT_PROPERTY_OVERRIDES += \
    camera.disable_zsl_mode=1 \
    vendor.camera.aux.packagelist=org.codeaurora.snapcam,com.android.camera,org.lineageos.snap

# Display density
PRODUCT_PROPERTY_OVERRIDES += \
    ro.sf.lcd_density=480

# Display post-processing
PRODUCT_PROPERTY_OVERRIDES += \
    ro.vendor.display.cabl=0 \
    vendor.display.enable_default_color_mode=0

# DRM
PRODUCT_PROPERTY_OVERRIDES += \
    drm.service.enabled=true

# FRP
PRODUCT_PROPERTY_OVERRIDES += \
    ro.frp.pst=/dev/block/bootdevice/by-name/frp \

# Graphics
PRODUCT_PROPERTY_OVERRIDES += \
    debug.egl.hw=0 \
    debug.sf.latch_unsignaled=0 \
    debug.sf.hw=0 \
    persist.demo.hdmirotationlock=false \
    ro.opengles.version=196610

# Media
PRODUCT_PROPERTY_OVERRIDES += \
    audio.offload.video=true

# NFC
PRODUCT_PROPERTY_OVERRIDES += \
    ro.hardware.nfc_nci=nqx.default

# Perf
PRODUCT_PROPERTY_OVERRIDES += \
    ro.vendor.extension_library=libqti-perfd-client.so \
    ro.vendor.qti.sys.fw.bg_apps_limit=38

# RIL
PRODUCT_PROPERTY_OVERRIDES += \
    persist.vendor.radio.apm_sim_not_pwdn=1 \
    persist.vendor.radio.custom_ecc=1 \
    persist.vendor.radio.rat_on=combine \
    persist.vendor.radio.sib16_support=1 \
    rild.libpath=/vendor/lib64/libril-qc-hal-qmi.so

PRODUCT_PROPERTY_OVERRIDES += \
    persist.backup.ntpServer="0.pool.ntp.org" \
    persist.backup.ntpServer=0.pool.ntp.org \
    persist.vendor.audio.fluence.audiorec=false \
    persist.vendor.bt.a2dp_offload_cap=sbc-aac \
    persist.vendor.qcomsysd.enabled=1 \
    persist.vendor.qti.games.gt.prof=1 \
    qemu.hw.mainkeys=0 \
    ro.build.shutdown_timeout=6 \
    ro.com.android.dataroaming=true \
    ro.com.google.clientidbase=android-razer \
    ro.com.google.gmsversion=9_201907 \
    ro.com.widevine.cachesize=16777216 \
    ro.cp_system_other_odex=1 \
    ro.opa.eligible_device=true \
    ro.setupwizard.mode=OPTIONAL \
    ro.vendor.at_library=libqti-at.so \
    ro.vendor.bt.bdaddr_path=/sys/module/fih_mfd/parameters/bt_mac \
    ro.vendor.razer.theme.version=1 \
    ro.wifi.power.reduction=1 \
    setupwizard.feature.predeferred_enabled=false \
    vendor.display.disable_fbid_cache=1 \
    vendor.gatekeeper.disable_spu=true

PRODUCT_DEFAULT_PROPERTY_OVERRIDES += \
    ro.oem_unlock_supported=1
