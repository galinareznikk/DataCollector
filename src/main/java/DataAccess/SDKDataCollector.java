package DataAccess;

import DataAccess.Exceptions.FetchingDataException;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.*;


import java.util.List;

public class SDKDataCollector implements DataCollector {
    private final SsmClient ssmClient;

    public SDKDataCollector(Region region) {
        this.ssmClient = SsmClient.builder().region(region).build();
    }

    @Override
    public List<ComplianceItem> listComplianceItems(final String[] resourceIds,
                                                    final ComplianceStringFilter... stringFilter)
            throws FetchingDataException {
        try {
            if (resourceIds.length != 1) {
                throw new IllegalArgumentException(
                        "Invalid resource ID: This method currently supports exactly one resource ID.");
            }

            // TODO: handle pagination
            // An authentication error will be thrown since credentials are not provided.
            ListComplianceItemsResponse response = ssmClient.listComplianceItems(ListComplianceItemsRequest.builder()
                    .resourceIds(resourceIds)
                    .filters(stringFilter)
                    .build());

            return response.complianceItems();
        } catch (Exception e) {
            throw new FetchingDataException("Error fetching compliance items: " + e.getMessage());
        }
    }


    public List<InstanceInformation> getInstancesInformation(final InstanceInformationStringFilter... stringFilter)
            throws FetchingDataException{
        try {
            // TODO: add pagination support
            // An authentication error will be thrown since credentials are not provided.
            DescribeInstanceInformationResponse response = ssmClient.describeInstanceInformation(
                    DescribeInstanceInformationRequest.builder().filters(stringFilter).build());
            return response.instanceInformationList();
        } catch (Exception e) {
            throw new FetchingDataException("Error fetching compliance items: " + e.getMessage());
        }
    }
}
