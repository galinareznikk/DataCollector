package DataAccess;

import DataAccess.Exceptions.FetchingDataException;
import software.amazon.awssdk.services.ssm.model.ComplianceItem;
import software.amazon.awssdk.services.ssm.model.ComplianceStringFilter;
import software.amazon.awssdk.services.ssm.model.InstanceInformation;
import software.amazon.awssdk.services.ssm.model.InstanceInformationStringFilter;

import java.util.List;


public interface DataCollector {
    List<ComplianceItem> listComplianceItems(final String[] resourceIds, final ComplianceStringFilter... stringFilter)
            throws FetchingDataException;
    List<InstanceInformation> getInstancesInformation(final InstanceInformationStringFilter... stringFilter)
            throws FetchingDataException;
}
