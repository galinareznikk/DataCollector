package Controls;

import Controls.Exceptions.VerificationControlException;
import DataAccess.Exceptions.FetchingDataException;
import DataAccess.SDKDataCollector;
import DataAccess.DataCollector;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ssm.model.*;

import java.util.List;
import java.util.stream.Collectors;

public class AssociationComplianceControl extends Control {
    private final DataCollector dataCollector;

    public AssociationComplianceControl(Region region) {
        this.dataCollector = new SDKDataCollector(region);
    }

    private boolean isResourceCompliant(final String resourceId) throws FetchingDataException {
        ComplianceStringFilter complianceTypeFilter = ComplianceStringFilter.builder()
                .key("ComplianceType")
                .type(ComplianceQueryOperatorType.EQUAL)
                .values("Association")
                .build();
        ComplianceStringFilter statusFilter = ComplianceStringFilter.builder()
                .key("Status")
                .type(ComplianceQueryOperatorType.NOT_EQUAL)
                .values("NON_COMPLIANT")
                .build();

        List<ComplianceItem> nonComplianceItems = dataCollector.listComplianceItems(
                new String[]{resourceId}, complianceTypeFilter, statusFilter);
        return !(nonComplianceItems.size()>0);
    }


    @Override
    public List<String> getViolateResources() throws VerificationControlException {
        InstanceInformationStringFilter ec2Filter = InstanceInformationStringFilter.builder()
                .key("ResourceType").values("EC2Instance").build();

        List<InstanceInformation> ec2Instances =  null;
        try {
            ec2Instances = dataCollector.getInstancesInformation(ec2Filter);
        } catch (FetchingDataException e) {
            // TODO: replace with logger
            System.out.println("Error occurred while fetching the instances list:" + e.getMessage());
            throw new VerificationControlException(e.getMessage());
        }

        try {
            List<String> nonCompliantEc2Instances = ec2Instances.stream()
                    .filter(ec2Instance -> {
                        try {
                            return !isResourceCompliant(ec2Instance.instanceId());
                        } catch (FetchingDataException e) {
                            String errorMessage = String.format(
                                    "Error occurred while getting the complete item for item %s: %s",
                                    ec2Instance.instanceId(),
                                    e.getMessage()
                            );
                            // TODO: replace with logger
                            System.out.println(errorMessage);
                            throw new RuntimeException(errorMessage);
                        }
                    })
                    .map(InstanceInformation::instanceId)
                    .collect(Collectors.toList());

            return nonCompliantEc2Instances;
        } catch (RuntimeException e) {
            throw new VerificationControlException(e.getMessage());
        }
    }
}
