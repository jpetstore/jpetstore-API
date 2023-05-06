package org.csu.jpetstoreapi.controller;


import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import org.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.csu.jpetstoreapi.entity.Sms;

import javax.validation.constraints.NotBlank;
import java.io.IOException;

@Controller
@Validated
@RequestMapping("/sms")
public class SmsController {

    @RequestMapping(value = "/sendCode",method = RequestMethod.POST)
    public void sms(@RequestParam @NotBlank(message="手机号不为空") String phone){
        int appid=1400818073;
        String appkey="260f7273e0a3e08f590f54565ce60259";
        int templateId=1785608;
        String smsSign="zengfs公众号";
        //伪验证码
        Sms sms=new Sms();
        sms.setMin(5);
        sms.setCode("8989");
        sms.setPhoneNumber(phone);

        try{
            String []params={sms.getCode(),Integer.toString(sms.getMin())};

            SmsSingleSender ssender=new SmsSingleSender(appid,appkey);
            SmsSingleSenderResult result=ssender.sendWithParam("86",sms.getPhoneNumber(),templateId,params,smsSign,"","");
            System.out.println(result);
        }catch (HTTPException e){
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
