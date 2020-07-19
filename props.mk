# Audio
PRODUCT_PROPERTY_OVERRIDES += \
    af.fast_track_multiplier=1 \
    audio.deep_buffer.media=true \
    audio.offload.buffer.size.kb=32 \
    audio.offload.gapless.enabled=true \
    audio.safemedia.force=true \
    av.offload.enable=true \
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
    vendor.voice.path.for.pcm.voip=false \
    vendor.audio.safx.pbe.enabled=true \
    vendor.audio.tunnel.encode=false \
    vendor.audio.use.sw.alac.decoder=true \
    vendor.audio.use.sw.ape.decoder=true

# Bluetooth
PRODUCT_PROPERTY_OVERRIDES += \
    bt.max.hfpclient.connections=1 \
    vendor.bluetooth.soc=cherokee \
    vendor.qcom.bluetooth.soc=cherokee

# Camera
PRODUCT_PROPERTY_OVERRIDES += \
    camera.disable_zsl_mode=true \
    vendor.camera.aux.packagelist=org.codeaurora.snapcam,com.android.camera,org.lineageos.snap

# CNE
PRODUCT_PROPERTY_OVERRIDES += \
    persist.vendor.cne.feature=1

# Data modules
PRODUCT_PROPERTY_OVERRIDES += \
    persist.data.df.dev_name=rmnet_usb0 \
    persist.vendor.data.mode=concurrent \
    ro.vendor.use_data_netmgrd=true

# Display density
PRODUCT_PROPERTY_OVERRIDES += \
    ro.sf.lcd_density=560

# Display post-processing
PRODUCT_PROPERTY_OVERRIDES += \
    ro.qualcomm.cabl=0 \
    ro.vendor.display.cabl=0 \
    vendor.display.enable_default_color_mode=0

# DRM
PRODUCT_PROPERTY_OVERRIDES += \
    drm.service.enabled=true

# FRP
PRODUCT_PROPERTY_OVERRIDES += \
    ro.frp.pst=/dev/block/bootdevice/by-name/frp \

# GNSS
PRODUCT_PROPERTY_OVERRIDES += \
    ro.hardware.flp=brcm \
    ro.hardware.gps=brcm

# Graphics
PRODUCT_PROPERTY_OVERRIDES += \
    debug.egl.hw=0 \
    debug.sf.enable_hwc_vds=1 \
    debug.sf.latch_unsignaled=0 \
    debug.sf.hw=0 \
    persist.demo.hdmirotationlock=false \
    persist.sys.sf.native_mode=3 \
    ro.opengles.version=196610

# Media
PRODUCT_PROPERTY_OVERRIDES += \
    audio.offload.video=true \
    media.settings.xml=/vendor/etc/media_profiles_vendor.xml

# Netflix custom property
PRODUCT_PROPERTY_OVERRIDES += \
    ro.netflix.bsp_rev=Q845-05000-1

# NFC
PRODUCT_PROPERTY_OVERRIDES += \
    ro.hardware.nfc_nci=nqx.default

# Perf
PRODUCT_PROPERTY_OVERRIDES += \
    ro.vendor.extension_library=libqti-perfd-client.so \
    ro.vendor.qti.core_ctl_min_cpu=2 \
    ro.vendor.qti.core_ctl_max_cpu=4 \
    ro.vendor.qti.sys.fw.bg_apps_limit=38 \
    vendor.iop.enable_uxe=1

# RCS and IMS
PRODUCT_PROPERTY_OVERRIDES += \
    persist.dbg.volte_avail_ovr=1 \
    persist.dbg.vt_avail_ovr=1 \
    persist.dbg.wfc_avail_ovr=1 \
    persist.rcs.supported=0

# RIL
PRODUCT_PROPERTY_OVERRIDES += \
    DEVICE_PROVISIONED=1 \
    persist.vendor.radio.apm_sim_not_pwdn=1 \
    persist.vendor.radio.custom_ecc=1 \
    persist.vendor.radio.rat_on=combine \
    persist.vendor.radio.sib16_support=1 \
    rild.libpath=/vendor/lib64/libril-qc-hal-qmi.so \
    ro.com.android.dataroaming=true \
    ro.telephony.default_network=20,20 \
    telephony.lteOnCdmaDevice=0

PRODUCT_PROPERTY_OVERRIDES += \
    config.disable_rtt=true \
    dev.pm.dyn_samplingrate=1 \
    media.aac_51_output_enabled=true \
    media.stagefright.audio.deep=true \
    media.stagefright.enable-aac=true \
    media.stagefright.enable-fma2dp=true \
    media.stagefright.enable-http=true \
    media.stagefright.enable-player=true \
    media.stagefright.enable-qcp=true \
    media.stagefright.enable-scan=true \
    mm.enable.smoothstreaming=true \
    mmp.enable.3g2=true \
    nfc.app_log_level=2 \
    persist.audio.fluence.speaker=true \
    persist.audio.fluence.voicecall=true \
    persist.audio.fluence.voicerec=false \
    persist.backup.ntpServer="0.pool.ntp.org" \
    persist.backup.ntpServer=0.pool.ntp.org \
    persist.debug.coresight.config=stm-events \
    persist.debug.wfd.enable=1 \
    persist.fuse_sdcard=true \
    persist.mm.enable.prefetch=true \
    persist.rild.nitz_long_ons_0="" \
    persist.rild.nitz_long_ons_1="" \
    persist.rild.nitz_long_ons_2="" \
    persist.rild.nitz_long_ons_3="" \
    persist.rild.nitz_plmn="" \
    persist.rild.nitz_short_ons_0="" \
    persist.rild.nitz_short_ons_1="" \
    persist.rild.nitz_short_ons_2="" \
    persist.rild.nitz_short_ons_3="" \
    persist.sys.force_sw_gles=1 \
    persist.sys.wfd.virtual=0 \
    persist.timed.enable=true \
    persist.vendor.audio.fluence.audiorec=false \
    persist.vendor.bt.a2dp_offload_cap=sbc-aac \
    persist.vendor.btstack.a2dp_offload_cap=sbc-aac \
    persist.vendor.btstack.enable.splita2dp=true \
    persist.vendor.data.iwlan.enable=true \
    persist.vendor.overlay.izat.optin=rro \
    persist.vendor.qcomsysd.enabled=1 \
    persist.vendor.qti.games.gt.prof=1 \
    persist.vendor.radio.atfwd.start=true \
    qcom.hw.aac.encoder=true \
    qemu.hw.mainkeys=0 \
    ro.audio.monitorOrientation=true \
    ro.audio.monitorRotation=true \
    ro.bluetooth.emb_wp_mode=true \
    ro.bluetooth.library_name=libbluetooth_qti.so \
    ro.bluetooth.wipower=true \
    ro.boot.wificountrycode=00 \
    ro.build.shutdown_timeout=6 \
    ro.com.google.gmsversion=9_201907 \
    ro.com.widevine.cachesize=16777216 \
    ro.config.alarm_vol_default=10 \
    ro.config.alarm_vol_steps=16 \
    ro.config.media_vol_default=7 \
    ro.config.media_vol_steps=18 \
    ro.config.vc_call_vol_default=6 \
    ro.config.vc_call_vol_steps=8 \
    ro.cp_system_other_odex=1 \
    ro.kernel.qemu.gles=0 \
    ro.location.osnlp.package=com.google.android.gms \
    ro.location.osnlp.region.package="" \
    ro.nfc.port=I2C \
    ro.oem_unlock_supported=1 \
    ro.opa.eligible_device=true \
    ro.qc.sdk.audio.fluencetype=none \
    ro.qc.sdk.audio.ssr=false \
    ro.setupwizard.mode=OPTIONAL \
    ro.setupwizard.require_network=junk \
    ro.setupwizard.rotation_locked=true \
    ro.vendor.at_library=libqti-at.so \
    ro.vendor.bt.bdaddr_path=/sys/module/fih_mfd/parameters/bt_mac \
    ro.vendor.config.notif_ring_def_vol=4 \
    ro.vendor.config.notif_ring_vol_steps=13 \
    ro.vendor.config.notif_ring_def_vol=4 \
    ro.vendor.config.notif_ring_vol_steps=13 \
    ro.vendor.qti.cgroup_follow.enable=true \
    ro.wifi.power.reduction=1 \
    setupwizard.feature.predeferred_enabled=false \
    sys.fflag.override.settings_systemui_theme=false \
    sys.qca1530=detect \
    tunnel.audio.encode=true \
    use.voice.path.for.pcm.voip=true \
    vendor.camera.dual_camera=true \
    vendor.display.disable_fbid_cache=1 \
    vendor.gatekeeper.disable_spu=true \
    vendor.mm.enable.qcom_parser=13631487 \
    vendor.perf.iop_v3.enable=1
    #vendor.display.res_switch_en=1
