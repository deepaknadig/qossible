package org.unl.cse.netgroup;

import org.onlab.osgi.DefaultServiceDirectory;
import org.onosproject.net.DeviceId;
import org.onosproject.net.config.NetworkConfigService;
import org.onosproject.net.config.basics.BasicDeviceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configures a REST POSTed Open vSwitch device to load the "ovs" driver
 */
public class QossibleOVSConfigurator {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private String ovsDeviceId;

    // The OVS device to be configured
    private DeviceId ovsDevice;

    // Constructor
    public QossibleOVSConfigurator(String ovsDeviceId) {
        this.ovsDeviceId = ovsDeviceId;
    }

    // Method to configure the OVS device and load the "ovs" driver.
    public void ConfigureOvsDevice() {
        // convert string to DeviceId
        ovsDevice = DeviceId.deviceId(this.ovsDeviceId);
        log.info("Configuring the Open vSwitch Device {}.", ovsDevice.toString());

        // TODO: 8/4/17 @deepak Add Device to a store to check if device is already configured.

        // configure the device
        NetworkConfigService configService = DefaultServiceDirectory.getService(NetworkConfigService.class);
        BasicDeviceConfig config = configService.addConfig(ovsDevice, BasicDeviceConfig.class);
        config.driver("ovs");
        configService.applyConfig(DeviceId.deviceId(this.ovsDeviceId),BasicDeviceConfig.class, config.node());
        log.info("Device {} configuration complete.", ovsDevice.toString());
    }
}