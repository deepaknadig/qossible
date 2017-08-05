package org.unl.cse.netgroup;

import org.onlab.osgi.DefaultServiceDirectory;
import org.onosproject.net.DeviceId;
import org.onosproject.net.config.NetworkConfigService;
import org.onosproject.net.config.basics.BasicDeviceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Deepak Nadig Anantha <deepnadig@gmail.com> on 8/4/17.
 */
public class GetOvsDeviceInfo {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private String ovsDeviceId;

    // The OVS device to be configured
    private DeviceId ovsDevice;

    // Constructor
    public GetOvsDeviceInfo(String ovsDeviceId) {
        this.ovsDeviceId = ovsDeviceId;
    }

    // Method to configure the OVS device and load the "ovs" driver.
    public void ConfigureOvsDevice() {
        // convert string to DeviceId
        ovsDevice = DeviceId.deviceId(this.ovsDeviceId);
        log.info("Configure OVS Device %s.", ovsDevice.toString());

        // TODO: 8/4/17 Add Device to a store to check if device is already configured. 

        // configure the device
        NetworkConfigService configService = DefaultServiceDirectory.getService(NetworkConfigService.class);
        BasicDeviceConfig config = configService.addConfig(ovsDevice, BasicDeviceConfig.class);
        config.driver("ovs");
        configService.applyConfig(DeviceId.deviceId(this.ovsDeviceId),BasicDeviceConfig.class, config.node());
        log.info("Device %s configuration complete.", ovsDevice.toString());
    }
}
