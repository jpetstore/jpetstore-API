package org.csu.jpetstoreapi.controller;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.zhenzi.sms.ZhenziSmsClient;
import org.csu.jpetstoreapi.common.CommonResponse;
import org.csu.jpetstoreapi.entity.Sms;
import org.csu.jpetstoreapi.entity.User;
import org.csu.jpetstoreapi.service.UserService;
import org.csu.jpetstoreapi.util.*;
import org.csu.jpetstoreapi.util.AuthCodeUtil;
import org.csu.jpetstoreapi.util.RandomNumberUtil;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.imageio.ImageIO;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotBlank;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Controller
@Validated
@RequestMapping("/account/")
public class UserController {

    @Autowired
    private UserService userService;

    //用户登录
    @PostMapping("login")
    @ResponseBody
    public CommonResponse<User>login(@RequestParam("id") @NotBlank(message = "用户名不能为空") String id,
                                     @RequestParam("password") @NotBlank(message = "密码不能为空") String password,
                                     HttpSession session){
        CommonResponse<User>result=userService.getAccountByUsernameAndPassword(id,password);
        if(result.isSuccess()){
            session.setAttribute("loginUser",result.getData());
        }
        return result;
    }

    //获得登入用户的用户信息
    @PostMapping("get_loginUser_info")
    @ResponseBody
    public CommonResponse<User> getLoginUserInfo(HttpSession session){
        User loginUser = (User) session.getAttribute("loginUser");
        if(loginUser !=null){
            return CommonResponse.createForSuccess("获取登录用户信息成功",loginUser);
        }
        else{
            return CommonResponse.createForError("未登录");
        }
    }


    //判断用户是否存在
    @GetMapping("idIsExist")
    @ResponseBody
    public void idIsExist(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        User user = userService.findUserById(id);
//        System.out.println("id= "+id);
        System.out.println(user);
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        if(id==""||id==null){
            out.print("Empty");
        }
        else if(user != null){
            out.print("Exist");
        }
        else {
            out.print("Not Exist");
        }
        out.flush();
        out.close();
    }

    //退出登录
    @GetMapping("signout")
    @ResponseBody
    public CommonResponse<User> signout(HttpServletRequest request, HttpServletResponse response)throws IOException {
        if(request.getSession().getAttribute("loginUser") != null) {
            request.getSession().removeAttribute("loginUser");
//            System.out.println("成功登出");
            return CommonResponse.createForSuccessMessage("成功退出登录");
        }else{
//            System.out.println("用户不存在，无法登出");
            return CommonResponse.createForError("未登录");
        }
    }

    //用户注册
    @PostMapping("register")
    @ResponseBody
    public CommonResponse<User> register(HttpServletRequest request,User user){

        String id=request.getParameter("id");
        String password=request.getParameter("password");
        String firstname=request.getParameter("firstname");
        String lastname=request.getParameter("lastname");
        String email=request.getParameter("email");
        String phone=request.getParameter("phone");
        String addr1=request.getParameter("addr1");
        String addr2=request.getParameter("addr2");
        String city=request.getParameter("city");
        String state=request.getParameter("state");
        String zip=request.getParameter("zip");
        String country=request.getParameter("country");
        String status=request.getParameter("status");
//        String languagepre=request.getParameter("languagepre");
        user.setId(id);
        user.setPassword(password);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmail(email);
        user.setPhone(phone);
        user.setAddr1(addr1);
        user.setAddr2(addr2);
        user.setCity(city);
        user.setState(state);
        user.setZip(zip);
        user.setCountry(country);
        user.setStatus(status);
//        user.setLanguagepre(languagepre);
        CommonResponse<User> response=userService.insertUser(user);
        return response;
    }

    //编辑信息
    @PostMapping("editAccount")
    @ResponseBody
    public CommonResponse<User> saveAccount(HttpServletRequest request,HttpSession session){

        User user = (User) session.getAttribute("loginUser");//当前登录的用户信息
        if(user==null){
            return CommonResponse.createForError("请先登录");
        }

//        String id=request.getParameter("id");
        String password=request.getParameter("password");
        String firstname=request.getParameter("firstname");
        String lastname=request.getParameter("lastname");
        String email=request.getParameter("email");
        String phone=request.getParameter("phone");
        String addr1=request.getParameter("addr1");
        String addr2=request.getParameter("addr2");
        String city=request.getParameter("city");
        String state=request.getParameter("state");
        String zip=request.getParameter("zip");
        String country=request.getParameter("country");
        String status=request.getParameter("status");
//        String languagepre=request.getParameter("languagepre");
//        user.setId(id);
        user.setPassword(password);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmail(email);
        user.setPhone(phone);
        user.setAddr1(addr1);
        user.setAddr2(addr2);
        user.setCity(city);
        user.setState(state);
        user.setZip(zip);
        user.setCountry(country);
        user.setStatus(status);

        CommonResponse<User> response=userService.updateUser(user);
        return response;
    }

    //图片验证码
    //todo:将值显示到前端
    @GetMapping("authCode")
    public void authCode(HttpSession session, HttpServletResponse response,Integer number) throws IOException {
        AuthCodeUtil authCodeUtil=new AuthCodeUtil();
        BufferedImage image = new BufferedImage(authCodeUtil.WIDTH,authCodeUtil.HEIGHT,BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        String authCode = "";
        authCodeUtil.setBackground(g);
        authCodeUtil.setBorder(g);
        authCodeUtil.setRandomLine(g);
        authCode = authCodeUtil.setWriteDate(g);
        System.out.println("*******************************************");
        System.out.println("当前验证码："+authCode);
        System.out.println("*******************************************");


        session.removeAttribute(authCode);
        session.setAttribute("authCode",authCode);
        response.setContentType("image/jpeg");
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-control","no-cache");
        response.setIntHeader("Expires",-1);
        g.dispose();
        ImageIO.write(image,"JPEG",response.getOutputStream());
    }

    //获取图片验证码
    @GetMapping("getAuthCode")
    @ResponseBody
    public CommonResponse<User> getAuthCode(HttpSession session){
        String authCode = (String)session.getAttribute("authCode");
//        System.out.println(authCode);
        if (authCode==null)return CommonResponse.createForError("验证码未创建");
        else {
            session.removeAttribute("authCode");
            return CommonResponse.createForSuccessMessage(authCode);
        }
    }


    //生成并发送手机验证码（手机登录）
    @PostMapping("phoneVCode")
    @ResponseBody
    public CommonResponse phoneCode(HttpServletRequest request,String phone){

        /*
        String apiUrl = "https://sms_developer.zhenzikj.com";
        String appId  = "111103";
        String appSecret = "761719c1-e3cc-41dc-9074-01744465caad";
        String reminder = null;
        String vCode = null;

        try{
            HttpSession session= request.getSession();
            session.removeAttribute("vCode");
            vCode = RandomNumberUtil.getRandomNumber();
            ZhenziSmsClient client = new ZhenziSmsClient(apiUrl, appId, appSecret);

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("number", phone);
            params.put("templateId", "8485");
            String[] templateParams = new String[1];
            templateParams[0] = vCode;
            System.out.println("验证码= "+vCode);
            params.put("templateParams", templateParams);
            String result = client.send(params);

            reminder = "验证码发送成功";
            request.setAttribute("reminder",reminder);
            request.getSession().setAttribute("vCode",vCode);

            System.out.println(result);

            return CommonResponse.createForSuccessMessage("验证码已成功发送，请注意查收！");

        }catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error","验证码发送失败");
            return CommonResponse.createForError("出了点问题，请刷新页面后重试！");
        }
        */

        HttpSession session= request.getSession();
        session.removeAttribute("vCode");
        String vCode = RandomNumberUtil.getRandomNumber();
        System.out.println("验证码= "+vCode);


        //腾讯云验证码
        int appid=1400818073;
        String appkey="260f7273e0a3e08f590f54565ce60259";
        int templateId=1785608;
        String smsSign="zengfs公众号";
        //赋值
        Sms sms=new Sms();
        sms.setMin(5);
        sms.setCode(vCode);
        sms.setPhoneNumber(phone);
        try{
            String []params={sms.getCode(),Integer.toString(sms.getMin())};

            SmsSingleSender ssender=new SmsSingleSender(appid,appkey);
            SmsSingleSenderResult result=ssender.sendWithParam("86",sms.getPhoneNumber(),templateId,params,smsSign,"","");

            String resultMsg=(String) result.errMsg;
            if(resultMsg=="手机号格式错误"||resultMsg.equals("手机号格式错误")){
                return CommonResponse.createForError(resultMsg);
            }
            session.setAttribute("vCode",vCode);
            return CommonResponse.createForSuccessMessage("验证码已成功发送，请注意查收！");
        }catch (HTTPException e){
            e.printStackTrace();
            return CommonResponse.createForError("短信发送失败");
        }catch (JSONException e){
            e.printStackTrace();
            return CommonResponse.createForError("短信发送失败");
        }catch (IOException e){
            e.printStackTrace();
            return CommonResponse.createForError("短信发送失败");
        }
    }


    //手机号登陆
    @PostMapping("signinPhone")
    @ResponseBody
    public CommonResponse signinPhone(HttpSession session,String phone,String vCode){
        String phoneVCode = (String)session.getAttribute("vCode");

        if(phoneVCode==null)return CommonResponse.createForError("验证码未创建");
        else {
            User user = userService.findUserByPhone(phone);
            if (user == null) {
                return CommonResponse.createForError("查无此人");
            } else if (!vCode.equals(phoneVCode)) {
                return CommonResponse.createForError("手机验证码有误，请重新输入！");
            } else {
                session.setAttribute("loginUser", user);
                session.removeAttribute("vCode");
                return CommonResponse.createForSuccess("登录成功",user);
            }

        }
    }

    //忘记密码发送新密码到手机号
    @PostMapping("/passwordMSG")
    @ResponseBody
    public CommonResponse passwordMSG(HttpServletRequest request, String phoneNumber, String id, Model model){

        String apiUrl = "https://sms_developer.zhenzikj.com";
        String appId  = "111103";
        String appSecret = "761719c1-e3cc-41dc-9074-01744465caad";
        String newPassword = "";

//        System.out.println(id);
        User user = userService.findUserById(id);
        System.out.println(user);

        if(user == null){
            return CommonResponse.createForError("用户名不存在！");
        } else if (!user.getPhone().equals(phoneNumber)) {
            return CommonResponse.createForError("用户名与手机号不匹配！");
        }else {
            try{
                newPassword = RandomNumberUtil.getRandomNumber();
                ZhenziSmsClient client = new ZhenziSmsClient(apiUrl, appId, appSecret);
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("number", phoneNumber);
                params.put("templateId", "8515");
                String[] templateParams = new String[1];
                templateParams[0] = newPassword;

                params.put("templateParams", templateParams);
                String result = client.send(params);

                System.out.println(result);

                user.setPassword(newPassword);
                return userService.updateUserById(user);
            }catch (Exception e) {
                e.printStackTrace();
                return CommonResponse.createForError("验证码发送失败！");
            }
        }
    }

    @PostMapping("/passwordMSGMAIL")
    @ResponseBody
    public CommonResponse passwordMSGMAIL(HttpServletRequest request, String email, String id, Model model){
        String apiUrl = "https://sms_developer.zhenzikj.com";
        String appId  = "111103";
        String appSecret = "761719c1-e3cc-41dc-9074-01744465caad";
        String newPassword = null;

        User user = userService.findUserById(id);

        if(user == null){
            return CommonResponse.createForError("用户名不存在！");
        } else if (!user.getEmail().equals(email)) {
            return CommonResponse.createForError("用户名与邮箱不匹配！");
        }else {

            try {

                newPassword = RandomNumberUtil.getRandomNumber();
                newPassword += RandomNumberUtil.getRandomNumber();

                JavaMailUtil.receiveMailAccount = email;

                Properties pops = new Properties();
                pops.setProperty("mail.debug","true");
                pops.setProperty("mail.smtp.auth","true");
                pops.setProperty("mail.host",JavaMailUtil.emailSMTPHost);
                pops.setProperty("mail.transport.protocol","smtp");
                Session session = Session.getInstance(pops);
                session.setDebug(true);
                String html = htmlTextResetPSW.htmlTextResetPSW(newPassword);
                MimeMessage message = JavaMailUtil.creatMimeMessage(session, JavaMailUtil.emailAccount,
                        JavaMailUtil.receiveMailAccount,html);
                Transport transport = session.getTransport();
                transport.connect(JavaMailUtil.emailAccount,JavaMailUtil.emailPassword);
                transport.sendMessage(message,message.getAllRecipients());
                transport.close();

                user.setPassword(newPassword);
                return userService.updateUserById_2(user);


            }catch (Exception e) {
                e.printStackTrace();
                return CommonResponse.createForError("验证码发送失败！");
            }
        }
    }


}
