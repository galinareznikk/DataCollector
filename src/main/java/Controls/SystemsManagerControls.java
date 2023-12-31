package Controls;

import Controls.AssociationComplianceControl;
import Controls.Control;
import Controls.Exceptions.VerificationControlException;
import software.amazon.awssdk.regions.Region;

import java.util.List;

public class SystemsManagerControls {
    private final Control ssm3Control;

    public SystemsManagerControls(Region region){
        this.ssm3Control = new AssociationComplianceControl(region);
    }

    public List<String> getResourcesViolateSSM3() throws VerificationControlException{
        return ssm3Control.getViolateResources();
    }
}
