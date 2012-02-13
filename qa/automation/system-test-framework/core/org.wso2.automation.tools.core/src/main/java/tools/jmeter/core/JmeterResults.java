package tools.jmeter.core;

import java.util.Map;

public class JmeterResults {
    public String _fileName;
    public String _testPlan;
    public boolean _executionState;
    public Map<String, String> _assertReport;

    public String get_fileName() {
        return _fileName;
    }

    public String get_testPlan() {
        return _testPlan;
    }

    public boolean getExecuteState() {
        return _executionState;
    }

    public Map<String, String> getAssertReport() {
        return _assertReport;
    }
}
