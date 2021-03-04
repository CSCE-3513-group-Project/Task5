package edu.uark.registerapp.controllers;

import edu.uark.registerapp.commands.employees.EmployeeSignInCommand;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uark.registerapp.commands.products.ProductQuery;
import edu.uark.registerapp.controllers.enums.ViewModelNames;
import edu.uark.registerapp.controllers.enums.ViewNames;
import edu.uark.registerapp.models.api.EmployeeSignIn;
import edu.uark.registerapp.models.api.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SignInRouteController {

    private static int existFlag = -1;

    public static int setExistFlag(int existFlagIn) {
        existFlag = existFlagIn;
        return existFlag;
    }
    private static int validFlag = -1;

    public static int setValidFlag(int validFlagIn) {
        validFlag = validFlagIn;
        return validFlag;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/") //opens either sign in html file or employee detail file when app starts
    public ModelAndView start() {
        if (existFlag == -1) //change to if "(existFlag == 1)" once employee functionality is done 
        {
            System.out.println("Modelandview.start 1");
            return (new ModelAndView(ViewNames.SIGN_IN.getViewName()))
                    .addObject(ViewModelNames.SIGNIN.getValue());//opens sign in html file

        } else {
            System.out.println("Modelandview.start 2");
            return (new ModelAndView(ViewNames.EMPLOYEE_DETAIL.getViewName()))
                    .addObject(ViewModelNames.EMPLOYEEDETAIL.getValue());//opens employee detail html file(not done) this registers a new employee.
        }
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/submit") //opens either sign in html file or employee detail file when app starts
    public ModelAndView startWithSubmit(@RequestParam Map<String, String> employeeCreds) {
        String ID;
        String password;
        System.out.println("STARTWITHSUMBIT");
        if (existFlag == -1) //change to if "(existFlag == 1)" once employee functionality is done 
        {
            System.out.println("Modelandview.startwithsubmit 1");
            ModelAndView obj = (new ModelAndView(ViewNames.SIGN_IN.getViewName()))
                    .addObject(ViewModelNames.SIGNIN.getValue());//opens sign in html file
            ID = employeeCreds.get("employeeID");
            password = employeeCreds.get("employeePswd");
//            System.out.println("ID:" + employeeCreds.get("employeeID"));
//            System.out.println("Password:" + employeeCreds.get("employeePswd"));
            System.out.println("ID:" + ID);
            System.out.println("Password:" + password);
            employeeCredentials.setEmployeeId(employeeCreds.get("employeeID"));
            employeeCredentials.setEmployeePassword(employeeCreds.get("employeePswd"));
            employeeSignInCommand.setEmployeeCredentials(employeeCredentials);
            employeeSignInCommand.execute();
//               Product test = new Product();
//               test.setLookupCode("lookupcode2");
//               ProductByLookupCodeQuery temp = new ProductByLookupCodeQuery();
//               temp.setLookupCode(test.getLookupCode());
//               temp.execute();
//            employeeCredentials.setEmployeeId(employeeID);
//            employeeCredentials.setEmployeePassword(password);
//            ModelAndView obj = (new ModelAndView(ViewNames.SIGN_IN.getViewName()))
//                    .addObject(ViewModelNames.SIGNIN.getValue());//opens sign in html file

            return obj;
        } else {
            System.out.println("Modelandview.startwithsubmit 2");
            return (new ModelAndView(ViewNames.EMPLOYEE_DETAIL.getViewName()))
                    .addObject(ViewModelNames.EMPLOYEEDETAIL.getValue());//opens employee detail html file(not done) this registers a new employee.
        }
    }

//    @RequestMapping(method = RequestMethod.POST, value = "/2", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//    public ModelAndView startWithEmployee(EmployeeSignIn signIn, HttpServletRequest request, @RequestParam(value = "employeeID", required = true) String employeeID, @RequestParam(value = "employeePswd", required = true) String employeePassword) {
//        if (validFlag == 1) {
//            return (new ModelAndView(ViewNames.MAIN_MENU.getViewName()))
//                    .addObject(ViewModelNames.MAINMENU.getValue());//opens main menu (main menu is not done)
//        } else {
//            return (new ModelAndView(ViewNames.SIGN_IN.getViewName()))
//                    .addObject(ViewModelNames.SIGNIN.getValue());//if password and username don't match the sign in menu pops back up
//        }
//    }
    // properties
    private EmployeeSignIn employeeCredentials = new EmployeeSignIn();
    private EmployeeSignInCommand employeeSignInCommand = new EmployeeSignInCommand();
}
