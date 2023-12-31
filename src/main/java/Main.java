import Controls.Exceptions.VerificationControlException;
import Controls.SystemsManagerControls;
import software.amazon.awssdk.regions.Region;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        Region region = Region.AF_SOUTH_1;
        SystemsManagerControls systemsManagerControls = new SystemsManagerControls(region);

        try {
            List<String> instancesViolatingSSM3 = systemsManagerControls.getResourcesViolateSSM3();
            System.out.println("Instances violating SSM3: " + instancesViolatingSSM3); // TODO: replace with logger
        } catch (VerificationControlException e) {
            System.out.println("An error occurred while evaluating the control."); // TODO: replace with logger
        }
    }
}
