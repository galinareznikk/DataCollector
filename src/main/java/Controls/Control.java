package Controls;

import Controls.Exceptions.VerificationControlException;
import DataAccess.Exceptions.FetchingDataException;

import java.util.List;

public abstract class Control {
    abstract List<String> getViolateResources() throws VerificationControlException;
}