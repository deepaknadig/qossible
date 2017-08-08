package org.unl.cse.netgroup;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.onlab.osgi.DefaultServiceDirectory;
import org.onosproject.net.Device;
import org.onosproject.net.DeviceId;
import org.onosproject.net.behaviour.PortConfigBehaviour;
import org.onosproject.net.behaviour.QosConfigBehaviour;
import org.onosproject.net.behaviour.QueueConfigBehaviour;
import org.onosproject.net.config.NetworkConfigService;
import org.onosproject.net.config.basics.BasicDeviceConfig;
import org.onosproject.net.device.DeviceService;
import org.onosproject.net.driver.DriverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configures a REST POSTed Open vSwitch device to load the "ovs" driver
 */
public class QossibleOVSConfigurator {

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected DriverService driverService;

    private final Logger log = LoggerFactory.getLogger(getClass());
    private String ovsDeviceId;

    // The OVS device to be configured
    private DeviceId ovsDevice;

    // Constructor
    public QossibleOVSConfigurator(String ovsDeviceId) {
        this.ovsDeviceId = ovsDeviceId;
        ovsDevice = DeviceId.deviceId(this.ovsDeviceId);
        log.debug("Device DEBUG: {}", ovsDevice.toString());
    }

    // Method to configure the Open vSwitch device and load the "ovs" driver.
    public void ConfigureOvsDevice() {
        // convert string to DeviceId
        log.info("Configuring the Open vSwitch Device {}.", ovsDevice.toString());

        // TODO: 8/4/17 @deepak Add Device to a store to check if device is already configured.

        // configure the device
        NetworkConfigService configService = DefaultServiceDirectory.getService(NetworkConfigService.class);
        BasicDeviceConfig config = configService.addConfig(ovsDevice, BasicDeviceConfig.class);
        config.driver("ovs");
        configService.applyConfig(ovsDevice, BasicDeviceConfig.class, config.node());

        // Wait until device is configured.
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("Device {} configuration complete.", ovsDevice.toString());
    }

    // Get current Open vSwitch Queue information.
    public void GetQueueInfo() {

        DeviceService deviceService = DefaultServiceDirectory.getService(DeviceService.class);
        Device device = deviceService.getDevice(ovsDevice);

        if (device == null) {
            log.error("Device {} is not configured.", device.toString());
            return;
        }

        log.info("Getting Configuration Info...");
        try {
            QueueConfigBehaviour queueConfig = device.as(QueueConfigBehaviour.class);
            log.info("QCONFIG INFO: " + String.valueOf(queueConfig.getQueues()));
            QosConfigBehaviour qosConfig = device.as(QosConfigBehaviour.class);
            PortConfigBehaviour portConfig = device.as(PortConfigBehaviour.class);

            queueConfig.getQueues().forEach(q -> {
                log.info("name={}, type={}, dscp={}, maxRate={}, minRate={}, pri={}, burst={}",
                         q.queueId(), q.type(), q.dscp(), q.maxRate(), q.minRate(),q.priority(), q.burst());
            });
        } catch (NullPointerException e) {
            throw new NullPointerException("Null Pointer Exception: Configurations not found!");
        }


    }
}
