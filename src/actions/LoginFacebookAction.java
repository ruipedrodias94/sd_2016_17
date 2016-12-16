package actions;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by Rui Pedro Dias on 15/12/2016.
 */
public class LoginFacebookAction extends ActionSupport implements SessionAware, ServletRequestAware, ServletResponseAware{

    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private HttpServletRequest httpServletRequest;
    private HttpServletResponse httpServletResponse;

    @Override
    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }

    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }
}
