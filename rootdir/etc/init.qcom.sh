#! /vendor/bin/sh

# Copyright (c) 2009-2016, The Linux Foundation. All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are met:
#     * Redistributions of source code must retain the above copyright
#       notice, this list of conditions and the following disclaimer.
#     * Redistributions in binary form must reproduce the above copyright
#       notice, this list of conditions and the following disclaimer in the
#       documentation and/or other materials provided with the distribution.
#     * Neither the name of The Linux Foundation nor
#       the names of its contributors may be used to endorse or promote
#       products derived from this software without specific prior written
#       permission.
#
# THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
# AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
# IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
# NON-INFRINGEMENT ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
# CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
# EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
# PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
# OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
# WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
# OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
# ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
#

start_msm_irqbalance()
{
	if [ -f /vendor/bin/msm_irqbalance ]; then
		start vendor.msm_irqbalance
	fi
}

echo 1 > /proc/sys/net/ipv6/conf/default/accept_ra_defrtr

start_msm_irqbalance

#
# Make modem config folder and copy firmware config to that folder for RIL
#
if [ -f /data/vendor/modem_config/ver_info.txt ]; then
    prev_version_info=`cat /data/vendor/modem_config/ver_info.txt`
else
    prev_version_info=""
fi

cur_version_info=`cat /vendor/firmware_mnt/verinfo/ver_info.txt`
if [ ! -f /vendor/firmware_mnt/verinfo/ver_info.txt -o "$prev_version_info" != "$cur_version_info" ]; then
    # add W for group recursively before delete
    chmod g+w -R /data/vendor/modem_config/*
    rm -rf /data/vendor/modem_config/*
    # preserve the read only mode for all subdir and files
    cp --preserve=m -dr /vendor/firmware_mnt/image/modem_pr/mcfg/configs/* /data/vendor/modem_config
    cp --preserve=m -d /vendor/firmware_mnt/verinfo/ver_info.txt /data/vendor/modem_config/
    cp --preserve=m -d /vendor/firmware_mnt/image/modem_pr/mbn_ota.txt /data/vendor/modem_config/
    # the group must be root, otherwise this script could not add "W" for group recursively
    chown -hR radio.root /data/vendor/modem_config/*
fi
chmod g-w /data/vendor/modem_config
setprop ro.vendor.ril.mbn_copy_completed 1

cur_fihmodel=`getprop ro.product.model.num`
if [ ! -f /data/misc/fih_mcfg/fih_model.txt ]; then
    echo $cur_fihmodel > /data/misc/fih_mcfg/fih_model.txt
    chmod 664 /data/misc/fih_mcfg/fih_model.txt
    chown system.oem_2951 /data/misc/fih_mcfg/fih_model.txt
fi

if [ -f /data/misc/fih_mcfg/ver_info.txt ]; then
    pre_fihver=`cat /data/misc/fih_mcfg/ver_info.txt`
else
    pre_fihver=""
fi

cur_fihver=`cat /firmware/verinfo/ver_info.txt`
if [ ! -f /firmware/verinfo/ver_info.txt -o "$pre_fihver" != "$cur_fihver" ]; then
    rm -rf /data/misc/fih_atl/modem_config
    mkdir /data/misc/fih_atl/modem_config
    cp -r /firmware/image/modem_pr/mcfg/configs/* /data/misc/fih_atl/modem_config
    chmod -R 770 /data/misc/fih_atl/modem_config
    chown -hR system.oem_2951 /data/misc/fih_atl/modem_config
    cp /firmware/verinfo/ver_info.txt /data/misc/fih_mcfg/ver_info.txt
    chown radio.oem_2951 /data/misc/fih_mcfg/ver_info.txt
fi
echo 0 > /data/vendor/radio/atl_complete
chmod 660 /data/vendor/radio/atl_complete
chown system.radio /data/vendor/radio/atl_complete
echo 0 > /data/misc/fih_mcfg/rfs_complete
chmod 666 /data/misc/fih_mcfg/rfs_complete
chown system.oem_2951 /data/misc/fih_mcfg/rfs_complete
echo -n > /data/misc/fih_mcfg/atl_log.txt
chmod 666 /data/misc/fih_mcfg/atl_log.txt
chown system.oem_2951 /data/misc/fih_mcfg/atl_log.txt
echo -n > /data/misc/fih_mcfg/mbn_0
chmod 664 /data/misc/fih_mcfg/mbn_0
chown system.oem_2951 /data/misc/fih_mcfg/mbn_0
echo -n > /data/misc/fih_mcfg/mbn_1
chmod 664 /data/misc/fih_mcfg/mbn_1
chown system.oem_2951 /data/misc/fih_mcfg/mbn_1

if [ "$pre_fihver" != "$cur_fihver" ]; then
    echo -n > /data/misc/fih_mcfg/fih_mcfg.lock
    chmod 444 /data/misc/fih_mcfg/fih_mcfg.lock
    chown system.oem_2951 /data/misc/fih_mcfg/fih_mcfg.lock
fi

# Check build variant for printk logging
# Current default minimum boot-time-default
buildvariant=`getprop ro.build.type`
case "$buildvariant" in
    "userdebug" | "eng")
        # Set default loglevel to KERN_INFO
        echo "6 6 1 7" > /proc/sys/kernel/printk
        ;;
    *)
        # Set default loglevel to KERN_WARNING
        echo "4 4 1 4" > /proc/sys/kernel/printk
        ;;
esac
