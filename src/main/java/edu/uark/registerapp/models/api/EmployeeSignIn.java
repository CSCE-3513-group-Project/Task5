package edu.uark.registerapp.models.api;

import org.apache.commons.lang3.StringUtils;

public class EmployeeSignIn extends ApiResponse{

	private String employeeId;
        private String employeePassword;
        
        public EmployeeSignIn() {
            employeeId = StringUtils.EMPTY;
            employeePassword = StringUtils.EMPTY;
        }

	public String setEmployeeId(final String employeeId) {
		this.employeeId = employeeId;
		return this.employeeId;
	}
	public String getEmployeeId() {
		return this.employeeId;
	}

	public String setEmployeePassword(final String employeePassword) {
		this.employeePassword = employeePassword;
		return this.employeePassword;
	}
	public String getEmployeePassword() {
		return this.employeePassword;
	}

    

}
